package ru.hse.icebergOrders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final int idLength = 10;
    private static final int volumeLength = 13;
    private static final int priceLength = 7;
    private static final int columns = 6;
    private static final int tableLength = columns + priceLength + volumeLength + idLength + 1;
    private static String getHeader() {
        return "+-----------------------------------------------------------------+" +
               "| BUY                            | SELL                           |" +
               "| Id       | Volume      | Price | Price | Volume      | Id       |" +
               "+-----------------------------------------------------------------+";
    }

    private static String lastLine() {
        return "+-----------------------------------------------------------------+";
    }

    private static String getFormattedNumber(int number, boolean useCommas, int length) {
        String numberInString = "";
        if (useCommas) {
            numberInString = String.join(",", getTriplesFromNumber(number));
        } else {
            numberInString = Integer.toString(number);
        }

        return " ".repeat(length - numberInString.length()) + numberInString;
    }

    private static List<String> getTriplesFromNumber(int number) {
        List<String> triples = new ArrayList<>();
        while (number > 0) {
            triples.add(Integer.toString(number % 1000));
            number /= 1000;
        }

        Collections.reverse(triples);
        return triples;
    }
}
