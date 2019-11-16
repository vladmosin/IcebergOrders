package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

public class OrderInfo implements Comparable<OrderInfo> {
    public static OrderInfo emptyInfo = new OrderInfo(OrderType.BUY, -1, -1, -1, -1);
    private static long globalTime = 0;

    private final int id;
    private int volume;
    private final int maxPeak;
    private int currentPeak;
    private final int price;
    private long timestamp;
    private final OrderType orderType;

    public OrderInfo(@NotNull OrderType orderType, int id, int volume, int price, int peak) {
        this.id = id;
        this.volume = volume;
        this.price = price;
        this.currentPeak = peak;
        this.maxPeak = peak;
        this.orderType = orderType;
        this.timestamp = globalTime;
        globalTime++;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getCurrentPeak() {
        return currentPeak;
    }

    public int getMaxPeak() {
        return maxPeak;
    }

    @NotNull public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public int compareTo(@NotNull OrderInfo o) {
        if (orderType == OrderType.BUY && o.orderType == OrderType.BUY) {
            return compareBuyOrders(o);
        } else if (orderType == OrderType.SELL && o.orderType == OrderType.SELL) {
            return compareSellOrders(o);
        }

        throw new IllegalArgumentException("Cannot compare Buy and Sell orders");

    }

    private int compareSellOrders(@NotNull OrderInfo o) {
        if (price < o.price) {
            return -1;
        } else if (timestamp < o.timestamp) {
            return -1;
        }

        return 1;
    }

    private int compareBuyOrders(@NotNull OrderInfo o) {
        if (price > o.price) {
            return -1;
        } else if (timestamp < o.timestamp) {
            return -1;
        }

        return 1;
    }

    private void updateTimestamp() {
        timestamp = globalTime;
        globalTime++;
    }

    public void tradePart(int tradeVolume) {
        volume -= tradeVolume;
        currentPeak -= tradeVolume;

        if (currentPeak == 0) {
            currentPeak = Math.max(volume, maxPeak);
            updateTimestamp();
        }
    }

    public boolean isEmpty() {
        return volume == 0;
    }
}
