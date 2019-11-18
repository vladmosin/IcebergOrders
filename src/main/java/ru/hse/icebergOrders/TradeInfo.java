package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

/**
 * Stores all information about trade
 * */
public class TradeInfo {
    private int buyId;
    private int sellId;
    private int price;
    private int volume;

    public TradeInfo(int buyId, int sellId, int price, int volume) {
        this.buyId = buyId;
        this.sellId = sellId;
        this.price = price;
        this.volume = volume;
    }

    /**
     * Adds given trade volume to volume of current trade
     * */
    public void concatenateTrades(@NotNull TradeInfo trade) {
        if (trade.price != price || trade.sellId != sellId || trade.buyId != buyId) {
            throw new IllegalArgumentException("Trades cannot be concatenated");
        }

        volume += trade.volume;
    }

    /**
     * orderType == BUY -> sellId
     * orderType == SELL -> buyId
     * */
    public int getOppositeId(@NotNull OrderType orderType) {
        if (orderType ==OrderType.BUY) {
            return sellId;
        }

        return buyId;
    }

    @Override
    @NotNull public String toString() {
        return buyId + "," + sellId + "," + price + "," + volume + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TradeInfo)) {
            return false;
        }

        var tradeInfo = (TradeInfo)o;
        return buyId == tradeInfo.buyId &&
               sellId == tradeInfo.sellId &&
               price == tradeInfo.price &&
               volume == tradeInfo.volume;
    }
}
