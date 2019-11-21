package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class Trading {
    public static void main(String[] args) {
        var in = new Scanner(System.in);
        var ordersHolder = new OrdersHolder();
        while (in.hasNext()) {
            String query = in.nextLine();
            if (isOrder(query)) {
                var orderInfo = parseQuery(query);
                var trades = ordersHolder.addOrderInfo(orderInfo);
                for (var trade : trades) {
                    System.out.print(trade.toString());
                }

                var buyInfos = ordersHolder.getBuyInfos();
                var sellInfos = ordersHolder.getSellInfos();
                System.out.print(Printer.getOrderBook(buyInfos, sellInfos));
            }
        }
    }

    private static boolean isOrder(@NotNull String query) {
        return !(query.length() == 0 || query.charAt(0) == ' ' || query.charAt(0) == '#');
    }

    @NotNull private static OrderInfo parseQuery(@NotNull String query) {
        var queryParts = query.split(",");
        var orderType = OrderType.orderTypeByString(queryParts[0]);
        int id = Integer.parseInt(queryParts[1]);
        int price = Integer.parseInt(queryParts[2]);
        int quantity = Integer.parseInt(queryParts[3]);
        int peak = quantity;

        if (queryParts.length == 5) {
            peak = Integer.parseInt(queryParts[4]);
        }

        return new OrderInfo(orderType, id, quantity, price, peak);
    }
}
