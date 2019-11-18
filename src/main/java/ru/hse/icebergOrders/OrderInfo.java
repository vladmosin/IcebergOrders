package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

/**
 * Holds all information about order
 * */
public class OrderInfo implements Comparable<OrderInfo> {
    /**
     * Special holder, which does not hold any information
     * */
    public static OrderInfo emptyInfo = new OrderInfo(OrderType.BUY, -1, -1, -1, -1);

    /**
     * Timestamp for orders, unique.
     * */
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
        this.currentPeak = volume;
        this.maxPeak = peak;
        this.orderType = orderType;
        this.timestamp = globalTime;
        globalTime++;
    }

    public OrderInfo(@NotNull OrderType orderType, int id, int volume, int price, int peak, int currentPeak) {
        this(orderType, id, volume, price, peak);
        this.currentPeak = currentPeak;
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

    /**
     * Decrease volume on given argument
     * If current peak decreases to zero, the order changes its timestamp to the greatest
     * and updates current peak
     * */
    public void tradePart(int tradeVolume) {
        volume -= tradeVolume;
        currentPeak -= tradeVolume;

        if (currentPeak == 0) {
            currentPeak = Math.min(volume, maxPeak);
            updateTimestamp();
        }
    }

    public boolean isEmpty() {
        return volume == 0;
    }

    @Override
    public boolean equals(@NotNull Object o) {
        if (!(o instanceof OrderInfo)) {
            return false;
        }
        var orderInfo = (OrderInfo)o;
        return id == orderInfo.id &&
               volume == orderInfo.volume &&
               currentPeak == orderInfo.currentPeak &&
               maxPeak == orderInfo.maxPeak &&
               price == orderInfo.price &&
               orderType == orderInfo.orderType;
    }

    /**
     * Sets peak volume
     * Should be called after aggressive trading was performed
     * */
    public void setCurrentPeak() {
        currentPeak = Math.min(volume, maxPeak);
    }
}
