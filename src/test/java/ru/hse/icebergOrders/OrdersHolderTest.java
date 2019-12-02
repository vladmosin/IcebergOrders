package ru.hse.icebergOrders;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class OrdersHolderTest {
    private static int id = 0;

    private boolean containersEqual(@NotNull Iterator<?> first, @NotNull Iterator<?> second) {
        while(first.hasNext() && second.hasNext()) {
            var firstElem = first.next();
            var secondElem = second.next();

            if (!firstElem.equals(secondElem)) {
                return false;
            }
        }

        return !first.hasNext() && !second.hasNext();
    }

    @Test
    void testAddingToOrderBookCell() {
        var ordersHolder = new OrdersHolder();

        int sellId1 = 0;
        int sellVolume1 = 100;
        int sellPeak1 = 10;
        int sellPrice1 = 30;

        var sellOrder1 = new OrderInfo(OrderType.SELL, sellId1, sellVolume1, sellPrice1, sellPeak1);
        ordersHolder.addOrderInfo(sellOrder1);

        int sellId2 = 1;
        int sellVolume2 = 10;
        int sellPeak2 = 3;
        int sellPrice2 = 8;

        var sellOrder2 = new OrderInfo(OrderType.SELL, sellId2, sellVolume2, sellPrice2, sellPeak2);
        ordersHolder.addOrderInfo(sellOrder2);

        var realSellOrders = ordersHolder.getSellInfos();
        var sellOrders = new TreeSet<OrderInfo>();

        sellOrders.add(sellOrder1);
        assertFalse(containersEqual(realSellOrders.iterator(), sellOrders.iterator()));
        sellOrders.add(sellOrder2);
        assertTrue(containersEqual(realSellOrders.iterator(), sellOrders.iterator()));
    }

    @Test
    void testAddingToOrderBookBuy() {
        var ordersHolder = new OrdersHolder();

        int buyId1 = 0;
        int buyVolume1 = 100;
        int buyPeak1 = 10;
        int buyPrice1 = 30;

        var buyOrder1 = new OrderInfo(OrderType.BUY, buyId1, buyVolume1, buyPrice1, buyPeak1);
        ordersHolder.addOrderInfo(buyOrder1);

        int buyId2 = 1;
        int buyVolume2 = 10;
        int buyPeak2 = 3;
        int buyPrice2 = 8;

        var buyOrder2 = new OrderInfo(OrderType.BUY, buyId2, buyVolume2, buyPrice2, buyPeak2);
        ordersHolder.addOrderInfo(buyOrder2);

        var realBuyOrders = ordersHolder.getBuyInfos();
        var buyOrders = new TreeSet<OrderInfo>();

        buyOrders.add(buyOrder1);
        assertFalse(containersEqual(realBuyOrders.iterator(), buyOrders.iterator()));
        buyOrders.add(buyOrder2);
        assertTrue(containersEqual(realBuyOrders.iterator(), buyOrders.iterator()));
    }

    @Test
    void testFullLimitOrder() {
        var ordersHolder = new OrdersHolder();

        int sellId = 0;
        int sellVolume = 100;
        int sellPeak = 100;
        int sellPrice = 30;

        var buyOrder1 = new OrderInfo(OrderType.SELL, sellId, sellVolume, sellPrice, sellPeak);
        ordersHolder.addOrderInfo(buyOrder1);

        int buyId = 1;
        int buyVolume = 100;
        int buyPeak = 100;
        int buyPrice = 40;

        var buyOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume, buyPrice, buyPeak);
        var trades = ordersHolder.addOrderInfo(buyOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, sellInfos.size());
        assertEquals(0, buyInfos.size());

        var tradeInfos = List.of(new TradeInfo(buyId, sellId, sellPrice, buyVolume));

        assertTrue(containersEqual(tradeInfos.iterator(), trades.iterator()));
    }

    @Test
    void testFullIcebergOrder() {
        var ordersHolder = new OrdersHolder();

        int sellId = 0;
        int sellVolume = 100;
        int sellPeak = 10;
        int sellPrice = 30;

        var buyOrder1 = new OrderInfo(OrderType.SELL, sellId, sellVolume, sellPrice, sellPeak);
        ordersHolder.addOrderInfo(buyOrder1);

        int buyId = 1;
        int buyVolume = 100;
        int buyPeak = 5;
        int buyPrice = 40;

        var buyOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume, buyPrice, buyPeak);
        var trades = ordersHolder.addOrderInfo(buyOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, sellInfos.size());
        assertEquals(0, buyInfos.size());

        var tradeInfos = List.of(new TradeInfo(buyId, sellId, sellPrice, buyVolume));

        assertTrue(containersEqual(tradeInfos.iterator(), trades.iterator()));
    }

    @Test
    void testPartOrder() {
        var ordersHolder = new OrdersHolder();

        int sellId = 0;
        int sellVolume = 1000;
        int sellPeak = 40;
        int sellPrice = 30;

        var sellOrder = new OrderInfo(OrderType.SELL, sellId, sellVolume, sellPrice, sellPeak);
        ordersHolder.addOrderInfo(sellOrder);

        int buyId = 1;
        int buyVolume = 100;
        int buyPeak = 3;
        int buyPrice = 40;

        var buyOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume, buyPrice, buyPeak);
        var trades = ordersHolder.addOrderInfo(buyOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, buyInfos.size());
        var tradeInfos = List.of(new TradeInfo(buyId, sellId, sellPrice, buyVolume));
        var leftOrder = new OrderInfo(OrderType.SELL, sellId, sellVolume - buyVolume,
                sellPrice, sellPeak, sellPeak - buyVolume % sellPeak);

        var sellOrders = new TreeSet<OrderInfo>();
        sellOrders.add(leftOrder);

        assertTrue(containersEqual(sellOrders.iterator(), sellInfos.iterator()));
        assertTrue(containersEqual(tradeInfos.iterator(), trades.iterator()));
    }

    @Test
    void testOrderingByPrice() {
        var ordersHolder = new OrdersHolder();

        int sellId1 = 0;
        int sellVolume1 = 100;
        int sellPeak1 = 100;
        int sellPrice1 = 30;

        int sellId2 = 2;
        int sellVolume2 = 100;
        int sellPeak2 = 100;
        int sellPrice2 = 29;

        var sellOrder1 = new OrderInfo(OrderType.SELL, sellId1, sellVolume1, sellPrice1, sellPeak1);
        var sellOrder2 = new OrderInfo(OrderType.SELL, sellId2, sellVolume2, sellPrice2, sellPeak2);
        ordersHolder.addOrderInfo(sellOrder1);
        ordersHolder.addOrderInfo(sellOrder2);

        int buyId = 1;
        int buyVolume = 100;
        int buyPeak = 3;
        int buyPrice = 40;

        var buyOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume, buyPrice, buyPeak);
        var trades = ordersHolder.addOrderInfo(buyOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, buyInfos.size());
        var tradeInfos = List.of(new TradeInfo(buyId, sellId2, sellPrice2, buyVolume));
        var leftOrder = new OrderInfo(OrderType.SELL, sellId1, sellVolume1, sellPrice1, sellPeak1);

        var sellOrders = new TreeSet<OrderInfo>();
        sellOrders.add(leftOrder);

        assertTrue(containersEqual(sellOrders.iterator(), sellInfos.iterator()));
        assertTrue(containersEqual(tradeInfos.iterator(), trades.iterator()));
    }

    @Test
    void testOrderingByTimestamp() {
        var ordersHolder = new OrdersHolder();

        int sellId1 = 0;
        int sellVolume1 = 100;
        int sellPeak1 = 100;
        int sellPrice1 = 30;

        int sellId2 = 2;
        int sellVolume2 = 100;
        int sellPeak2 = 100;
        int sellPrice2 = 30;

        var sellOrder1 = new OrderInfo(OrderType.SELL, sellId1, sellVolume1, sellPrice1, sellPeak1);
        var sellOrder2 = new OrderInfo(OrderType.SELL, sellId2, sellVolume2, sellPrice2, sellPeak2);
        ordersHolder.addOrderInfo(sellOrder1);
        ordersHolder.addOrderInfo(sellOrder2);

        int buyId = 1;
        int buyVolume = 100;
        int buyPeak = 3;
        int buyPrice = 40;

        var buyOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume, buyPrice, buyPeak);
        var trades = ordersHolder.addOrderInfo(buyOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, buyInfos.size());
        var tradeInfos = List.of(new TradeInfo(buyId, sellId1, sellPrice1, buyVolume));
        var leftOrder = new OrderInfo(OrderType.SELL, sellId2, sellVolume2, sellPrice2, sellPeak2);

        var sellOrders = new TreeSet<OrderInfo>();
        sellOrders.add(leftOrder);

        assertTrue(containersEqual(sellOrders.iterator(), sellInfos.iterator()));
        assertTrue(containersEqual(tradeInfos.iterator(), trades.iterator()));
    }

    @Test
    void testTimestampUpdateAfterTrade() {
        var ordersHolder = new OrdersHolder();

        int sellId1 = 0;
        int sellVolume1 = 100;
        int sellPeak1 = 1;
        int sellPrice1 = 30;

        int sellId2 = 2;
        int sellVolume2 = 100;
        int sellPeak2 = 100;
        int sellPrice2 = 30;

        var sellOrder1 = new OrderInfo(OrderType.SELL, sellId1, sellVolume1, sellPrice1, sellPeak1);
        var sellOrder2 = new OrderInfo(OrderType.SELL, sellId2, sellVolume2, sellPrice2, sellPeak2);
        ordersHolder.addOrderInfo(sellOrder1);
        ordersHolder.addOrderInfo(sellOrder2);

        int buyId = 1;
        int buyVolume = 100;
        int buyPeak = 3;
        int buyPrice = 40;

        var buyOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume, buyPrice, buyPeak);
        var trades = ordersHolder.addOrderInfo(buyOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, buyInfos.size());
        var tradeInfos = List.of(new TradeInfo(buyId, sellId1, sellPrice1, sellPeak1),
                                 new TradeInfo(buyId, sellId2, sellPrice2, buyVolume - sellPeak1));

        var leftOrder2 = new OrderInfo(OrderType.SELL, sellId2, sellVolume2 + sellPeak1 - buyVolume,
                sellPrice2, sellPeak2, sellVolume2 + sellPeak1 - buyVolume);
        var leftOrder1 = new OrderInfo(OrderType.SELL, sellId1, sellVolume1 - sellPeak1,
                sellPrice1, sellPeak1, sellPeak1);

        var sellOrders = new TreeSet<OrderInfo>();
        sellOrders.add(leftOrder1);
        sellOrders.add(leftOrder2);

        assertTrue(containersEqual(sellOrders.iterator(), sellInfos.iterator()));
        assertTrue(containersEqual(tradeInfos.iterator(),
                trades.iterator()));
    }

    @Test
    void testTradingWithFullVolumeBuy() {
        var ordersHolder = new OrdersHolder();

        int sellId1 = 0;
        int sellVolume1 = 100;
        int sellPeak1 = 20;
        int sellPrice1 = 30;

        int sellId2 = 2;
        int sellVolume2 = 100;
        int sellPeak2 = 30;
        int sellPrice2 = 30;

        var sellOrder1 = new OrderInfo(OrderType.SELL, sellId1, sellVolume1, sellPrice1, sellPeak1);
        var sellOrder2 = new OrderInfo(OrderType.SELL, sellId2, sellVolume2, sellPrice2, sellPeak2);
        ordersHolder.addOrderInfo(sellOrder1);
        ordersHolder.addOrderInfo(sellOrder2);

        int buyId = 1;
        int buyVolume = 1000;
        int buyPeak = 15;
        int buyPrice = 40;

        var buyOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume, buyPrice, buyPeak);
        var trades = ordersHolder.addOrderInfo(buyOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, sellInfos.size());
        var tradeInfos = List.of(new TradeInfo(buyId, sellId1, sellPrice1, sellVolume1),
                new TradeInfo(buyId, sellId2, sellPrice2, sellVolume1));

        var leftOrder = new OrderInfo(OrderType.BUY, buyId, buyVolume - sellVolume1 - sellVolume2,
                buyPrice, buyPeak, buyPeak);
        var buyOrders = new TreeSet<OrderInfo>();
        buyOrders.add(leftOrder);

        assertTrue(containersEqual(buyOrders.iterator(), buyInfos.iterator()));
        assertTrue(containersEqual(tradeInfos.iterator(),
                trades.iterator()));
    }

    @Test
    void testTradingWithFullVolumeSell() {
        var ordersHolder = new OrdersHolder();

        int buyId1 = 0;
        int buyVolume1 = 100;
        int buyPeak1 = 20;
        int buyPrice1 = 30;

        int buyId2 = 2;
        int buyVolume2 = 100;
        int buyPeak2 = 30;
        int buyPrice2 = 30;

        var buyOrder1 = new OrderInfo(OrderType.BUY, buyId1, buyVolume1, buyPrice1, buyPeak1);
        var buyOrder2 = new OrderInfo(OrderType.BUY, buyId2, buyVolume2, buyPrice2, buyPeak2);
        ordersHolder.addOrderInfo(buyOrder1);
        ordersHolder.addOrderInfo(buyOrder2);

        int sellId = 1;
        int sellVolume = 1000;
        int sellPeak = 15;
        int sellPrice = 20;

        var sellOrder = new OrderInfo(OrderType.SELL, sellId, sellVolume, sellPrice, sellPeak);
        var trades = ordersHolder.addOrderInfo(sellOrder);

        var sellInfos = ordersHolder.getSellInfos();
        var buyInfos = ordersHolder.getBuyInfos();

        assertEquals(0, buyInfos.size());
        var tradeInfos = List.of(new TradeInfo(buyId1, sellId, sellPrice, buyVolume1),
                new TradeInfo(buyId2, sellId, sellPrice, buyVolume1));

        var leftOrder = new OrderInfo(OrderType.SELL, sellId, sellVolume - buyVolume1 - buyVolume2,
                sellPrice, sellPeak, sellPeak);
        var sellOrders = new TreeSet<OrderInfo>();
        sellOrders.add(leftOrder);

        assertTrue(containersEqual(sellOrders.iterator(), sellInfos.iterator()));
        assertTrue(containersEqual(tradeInfos.iterator(),
                trades.iterator()));
    }

    @Test
    void stressTest() {
        int experiments = 100;
        int iterations = 10000;
        long seed = 239;

        var random = new Random(seed);

        for (int i = 0; i < experiments; i++) {
            var ordersHolder = new OrdersHolder();
            for (int j = 0; j < iterations; j++) {
                var orderInfo = generateRandomOrderInfo(random);
                var trades = ordersHolder.addOrderInfo(orderInfo);

                assertTrue(isSortedByPrice(trades));
                if (trades.size() > 0 && ordersHolder.getBuyInfos().size() > 0) {
                    assertTrue(trades.get(trades.size() - 1).getPrice() <= ordersHolder.getBuyInfos().first().getPrice());
                }

                if (trades.size() > 0 && ordersHolder.getSellInfos().size() > 0) {
                    assertTrue(trades.get(trades.size() - 1).getPrice() <= ordersHolder.getSellInfos().first().getPrice());
                }
            }
        }
    }

    @NotNull private OrderInfo generateRandomOrderInfo(@NotNull Random random) {
        int maxPrice = 100;
        int maxVolume = 100;

        int type = random.nextInt(1);
        int price = random.nextInt(maxPrice) + 1;
        int volume = random.nextInt(maxVolume) + 1;
        int peak = random.nextInt(volume) + 1;
        int orderId = id++;

        OrderType orderType = OrderType.SELL;
        if (type == 0) {
            orderType = OrderType.BUY;
        }

        return new OrderInfo(orderType, id, volume, price, peak);
    }

    private boolean isSortedByPrice(@NotNull List<TradeInfo> tradeInfos) {
        int minPrice = -1;
        for (var tradeInfo : tradeInfos) {
            if (minPrice == -1 || tradeInfo.getPrice() <= minPrice) {
                minPrice = tradeInfo.getPrice();
            } else {
                return false;
            }
        }

        return true;
    }


}