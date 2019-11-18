package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

/**
 * Stores order type: sell or buy
 * */
public enum OrderType {
    SELL,
    BUY;

    /**
     * Parses type from string: S -> SELL, B -> BUY
     * */
    @NotNull public static OrderType orderTypeByString(@NotNull String string) {
        if (string.equals("S")) {
            return SELL;
        } else if (string.equals("B")) {
            return BUY;
        }

        throw new IllegalArgumentException("Inappropriate string: " + string);
    }
}
