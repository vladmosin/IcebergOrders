package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class Printer {
    private static final int idLength = 10;
    private static final int volumeLength = 13;
    private static final int priceLength = 7;
    private static final String emptyRecordSell =
            "|" + " ".repeat(priceLength) + "|" + " ".repeat(volumeLength) + "|" + " ".repeat(idLength) + "|\n";
    private static final String emptyRecordBuy =
            "|" + " ".repeat(idLength) + "|" + " ".repeat(volumeLength) + "|" + " ".repeat(priceLength);
    private static String header = "+-----------------------------------------------------------------+\n" +
                                   "| BUY                            | SELL                           |\n" +
                                   "| Id       | Volume      | Price | Price | Volume      | Id       |\n" +
                                   "+-----------------------------------------------------------------+\n";

    private static String lastLine = "+-----------------------------------------------------------------+\n";

    @NotNull public static String getOrderBook(
            @NotNull TreeSet<OrderInfo> buyInfos, @NotNull TreeSet<OrderInfo> sellInfos) {
        var buyInfosIterator = buyInfos.iterator();
        var sellInfosIterator = sellInfos.iterator();
        var orderBook = new StringBuilder(header);

        while(buyInfosIterator.hasNext() || sellInfosIterator.hasNext()) {
            var buyInfo = OrderInfo.emptyInfo;
            var sellInfo = OrderInfo.emptyInfo;

            if (buyInfosIterator.hasNext()) {
                buyInfo = buyInfosIterator.next();
            }

            if (sellInfosIterator.hasNext()) {
                sellInfo = sellInfosIterator.next();
            }

            orderBook.append(getRecord(buyInfo, sellInfo));
        }

        return orderBook.append(lastLine).toString();
    }

    @NotNull public static String getTrade(int buyId, int sellId, int price, int quantity) {
        return buyId + "," + sellId + "," + price + "," + quantity + "\n";
    }

    @NotNull private static String getFormattedNumber(int number, boolean useCommas, int length) {
        String numberInString;
        if (useCommas) {
            numberInString = String.join(",", getTriplesFromNumber(number));
        } else {
            numberInString = Integer.toString(number);
        }

        return "|" + " ".repeat(length - numberInString.length()) + numberInString;
    }

    @NotNull private static List<String> getTriplesFromNumber(int number) {
        var triples = new ArrayList<String>();
        while (number > 999) {
            triples.add(getLastTriple(number));
            number /= 1000;
        }

        triples.add(Integer.toString(number));

        Collections.reverse(triples);
        return triples;
    }

    private static String getLastTriple(int number) {
        String lastDigit = Integer.toString(number % 10);
        String middleDigit = Integer.toString(number / 10 % 10);
        String firstDigit = Integer.toString(number / 100 % 10);

        return firstDigit + middleDigit + lastDigit;
    }

    @NotNull private static String getRecord(@NotNull OrderInfo buyInfo, @NotNull OrderInfo sellInfo) {
        return getRecordBuy(buyInfo) + getRecordSell(sellInfo);
    }

    @NotNull private static String getRecordBuy(@NotNull OrderInfo buyInfo) {
        if (buyInfo == OrderInfo.emptyInfo) {
            return emptyRecordBuy;
        }
        return getFormattedNumber(buyInfo.getId(), false, idLength) +
               getFormattedNumber(buyInfo.getCurrentPeak(), true, volumeLength) +
               getFormattedNumber(buyInfo.getPrice(), true, priceLength);
    }

    @NotNull private static String getRecordSell(@NotNull OrderInfo sellInfo) {
        if (sellInfo == OrderInfo.emptyInfo) {
            return emptyRecordSell;
        }
        return getFormattedNumber(sellInfo.getPrice(), true, priceLength) +
               getFormattedNumber(sellInfo.getCurrentPeak(), true, volumeLength) +
               getFormattedNumber(sellInfo.getPrice(), false, idLength) + "|\n";
    }
}
