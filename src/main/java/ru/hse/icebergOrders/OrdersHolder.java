package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Stores all active orders and performs trades
 * */
public class OrdersHolder {
    /**
     * Stores all buy orders
     * */
    @NotNull private TreeSet<@NotNull OrderInfo> buyInfos = new TreeSet<>();

    /**
     * Stores all sell orders
     * */
    @NotNull private TreeSet<@NotNull OrderInfo> sellInfos = new TreeSet<>();

    /**
     * Adds given order to currently observed orders
     * @return all trades, performed after adding given order
     * */
    @NotNull public Collection<TradeInfo> addOrderInfo(@NotNull OrderInfo orderInfo) {
        if (orderInfo.getOrderType() == OrderType.BUY) {
            buyInfos.add(orderInfo);
        } else {
            sellInfos.add(orderInfo);
        }

        var performedTrades = performTrades();
        orderInfo.setCurrentPeak();
        return joinTrades(performedTrades, orderInfo.getOrderType());
    }

    /**
     * Joins trades with same buyId and sellId
     * */
    @NotNull private Collection<TradeInfo> joinTrades(@NotNull List<TradeInfo> performedTrades,
                                                      @NotNull OrderType addedOrderType) {
        var joinedTrades = new HashMap<Integer, TradeInfo>();

        for (var trade : performedTrades) {
            int notAddedOrderId = trade.getOppositeId(addedOrderType);
            if (joinedTrades.containsKey(notAddedOrderId)) {
                joinedTrades.get(notAddedOrderId).concatenateTrades(trade);
            } else {
                joinedTrades.put(notAddedOrderId, trade);
            }
        }

        return joinedTrades.values();
    }

    /**
     * Performs all trades which occurs after adding new order
     * */
    @NotNull private List<TradeInfo> performTrades() {
        var trades = new ArrayList<TradeInfo>();
        while (!buyInfos.isEmpty() && !sellInfos.isEmpty()) {
            if (buyInfos.first().getPrice() < sellInfos.first().getPrice()) {
                break;
            }
            trades.add(processTrade());
        }

        return trades;
    }

    /**
     * Processes one trade
     * */
    @NotNull private TradeInfo processTrade() {
        var buyInfo = buyInfos.pollFirst();
        var sellInfo = sellInfos.pollFirst();

        if (buyInfo == null || sellInfo == null) {
            throw new IllegalStateException("In process trade: trade list is empty");
        }

        int tradeVolume = Math.min(buyInfo.getCurrentPeak(), sellInfo.getCurrentPeak());
        int price = sellInfo.getPrice();

        buyInfo.tradePart(tradeVolume);
        sellInfo.tradePart(tradeVolume);

        if (!buyInfo.isEmpty()) {
            buyInfos.add(buyInfo);
        }

        if (!sellInfo.isEmpty()) {
            sellInfos.add(sellInfo);
        }

        return new TradeInfo(buyInfo.getId(), sellInfo.getId(), price, tradeVolume);
    }

    /**
     * Returns buy infos from current order book
     * */
    @NotNull public TreeSet<OrderInfo> getBuyInfos() {
        return buyInfos;
    }

    /**
     * Returns sell infos from current order book
     * */
    @NotNull public TreeSet<OrderInfo> getSellInfos() {
        return sellInfos;
    }
}
