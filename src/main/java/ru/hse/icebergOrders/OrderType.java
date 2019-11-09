package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

public enum OrderType {
    SELL,
    BUY;

    @NotNull public static OrderType orderTypeByString(@NotNull String string) {
        if (string.equals("S")) {
            return SELL;
        } else if (string.equals("B")) {
            return BUY;
        }

        throw new IllegalArgumentException("Inappropriate string: " + string);
    }
}
