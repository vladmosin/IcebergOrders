package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class OrdersHolder {
    private class TradeInfo {
        @NotNull private String tradeMessage;
        private OrderInfo remainingOrderInfo;

        private TradeInfo(@NotNull String tradeMessage, OrderInfo remainingOrderInfo) {
            this.tradeMessage = tradeMessage;
            this.remainingOrderInfo = remainingOrderInfo;
        }
    }

    @NotNull private TreeSet<@NotNull OrderInfo> buyInfos = new TreeSet<>();
    @NotNull private TreeSet<@NotNull OrderInfo> sellInfos = new TreeSet<>();

    @NotNull public List<String> addOrderInfo(@NotNull OrderInfo orderInfo) {
        if (orderInfo.getOrderType() == OrderType.BUY) {
            buyInfos.add(orderInfo);
        } else {
            sellInfos.add(orderInfo);
        }

        return performTrades();
    }

    @NotNull private List<String> performTrades() {
        var trades = new ArrayList<String>();
        while (!buyInfos.isEmpty() && !sellInfos.isEmpty()) {
            if (buyInfos.first().getPrice() < sellInfos.first().getPrice()) {
                break;
            }

            var tradeInfo = processTrade();
            var remainingOrderInfo = tradeInfo.remainingOrderInfo;
            trades.add(tradeInfo.tradeMessage);

            if (remainingOrderInfo != null) {
                if (remainingOrderInfo.getOrderType() == OrderType.BUY) {
                    buyInfos.add(remainingOrderInfo);
                } else {
                    sellInfos.add(remainingOrderInfo);
                }
            }
        }

        return trades;
    }

    @NotNull private TradeInfo processTrade() {
        var buyInfo = buyInfos.pollFirst();
        var sellInfo = sellInfos.pollFirst();

        int tradeVolume = Math.min(buyInfo.getVolume(), sellInfo.getVolume());
        int price = buyInfo.getPrice();
        OrderInfo remainingOrderInfo = null;

        if (buyInfo.getVolume() > sellInfo.getVolume()) {
            remainingOrderInfo = new OrderInfo(OrderType.BUY, buyInfo.getId(), buyInfo.getVolume() - tradeVolume,
                    buyInfo.getPrice(), buyInfo.getVolume() - tradeVolume);
        } else if (buyInfo.getVolume() < sellInfo.getVolume()) {
            remainingOrderInfo = new OrderInfo(OrderType.SELL, sellInfo.getId(),
                    sellInfo.getVolume() - tradeVolume, sellInfo.getPrice(),
                    Math.min(sellInfo.getPeak(), sellInfo.getVolume() - tradeVolume));
        }

        return new TradeInfo(Printer.getTrade(buyInfo.getId(), sellInfo.getId(), price, tradeVolume), remainingOrderInfo);
    }

    @NotNull public String getOrderBook() {
        return Printer.getOrderBook(buyInfos, sellInfos);
    }
}
