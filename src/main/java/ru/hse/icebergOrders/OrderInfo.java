package ru.hse.icebergOrders;

public class OrderInfo {
    private int buyId;
    private int buyVolume;
    private int buyPrice;
    private int sellPrice;
    private int sellVolume;
    private int sellId;

    public OrderInfo(int buyId, int buyVolume, int buyPrice,
                     int sellId, int sellVolume, int sellPrice) {
        this.buyId = buyId;
        this.buyVolume = buyVolume;
        this.buyPrice = buyPrice;
        this.sellId = sellId;
        this.sellVolume = sellVolume;
        this.sellPrice = sellPrice;
    }

    public int getBuyId() {
        return buyId;
    }

    public int getBuyVolume() {
        return buyVolume;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getSellVolume() {
        return sellVolume;
    }

    public int getSellId() {
        return sellId;
    }
}
