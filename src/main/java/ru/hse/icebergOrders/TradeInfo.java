package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

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

    public void concatenateTrades(@NotNull TradeInfo trade) {
        if (trade.price != price || trade.sellId != sellId || trade.buyId != buyId) {
            throw new IllegalArgumentException("Trades cannot be concatenated");
        }

        volume += trade.volume;
    }

    public int getOppositeId(@NotNull OrderType orderType) {
        if (orderType ==OrderType.BUY) {
            return sellId;
        }

        return buyId;
    }

    @Override
    @NotNull public String toString() {
        return Printer.getTrade(buyId, sellId, price, volume);
    }
}
