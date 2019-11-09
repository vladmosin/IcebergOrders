package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

public class OrderInfo implements Comparable<OrderInfo> {
    public static OrderInfo emptyInfo = new OrderInfo(OrderType.BUY, -1, -1, -1, -1);

    private int id;
    private int volume;
    private int peak;
    private int price;
    private int timestamp;
    private OrderType orderType;

    public OrderInfo(@NotNull OrderType orderType, int id, int volume, int price, int peak) {
        this.id = id;
        this.volume = volume;
        this.price = price;
        this.peak = peak;
        this.orderType = orderType;
    }

    public int getId() {
        return id;
    }

    public int getVolume() {
        return volume;
    }

    public int getPrice() {
        return price;
    }

    public int getPeak() {
        return peak;
    }

    @NotNull public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public int compareTo(@NotNull OrderInfo o) {
        if (price < o.price) {
            return -1;
        } else if (price == o.price) {
            if (peak > o.peak) {
                return -1;
            } else if (timestamp < o.timestamp) {
                return -1;
            }
        }

        return 1;
    }
}
