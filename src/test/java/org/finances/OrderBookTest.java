package org.finances;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderBookTest {
    private OrderBook orderBook;

    @Before
    public void setUp() {
        orderBook = new OrderBook();
    }

    /**
     * Should handle and throw an exception when the order cannot be allocated
     */
    @Test
    public void handleOrderWhenOrderCannotBeAllocatedThenThrowException() {
        try {
            Order order = new Order("BUY,1,100,10");
            orderBook.handle(order);
            fail("Expected OrderNotHandledException to be thrown");
        } catch (OrderException e) {
            fail("Unexpected OrderException: " + e.getMessage());
        }
    }

    /**
     * Should handle and allocate the order when there are matching orders available
     */
    @Test
    public void handleOrderWhenMatchingOrdersAvailable() {
        try {
            Order buyOrder = new Order("BUY,1,100,50");
            Order sellOrder = new Order("SELL,2,90,30");
            orderBook.handle(buyOrder);
            orderBook.handle(sellOrder);

            assertEquals(20, (int) buyOrder.getQuantity());
            assertEquals(0, (int) sellOrder.getQuantity());
        } catch (OrderException e) {
            fail("Unexpected OrderException: " + e.getMessage());
        }
    }

    /**
     * Should handle and execute orders when the order quantity becomes zero
     */
    @Test
    public void handleOrderWhenOrderQuantityBecomesZero() {
        try {
            Order buyOrder1 = new Order("BUY,1,100,50");
            Order sellOrder1 = new Order("SELL,2,100,50");
            orderBook.handle(buyOrder1);
            orderBook.handle(sellOrder1);

            assertEquals(0, buyOrder1.getQuantity().intValue());
            assertEquals(0, sellOrder1.getQuantity().intValue());
        } catch (OrderException e) {
            fail("Unexpected OrderException: " + e.getMessage());
        }
    }

    /**
     * Should handle and add the order to the list when there are no matching orders available
     */
    @Test
    public void handleOrderWhenNoMatchingOrdersAvailable() {
        try {
            Order buyOrder = new Order("BUY,1,100,10");
            orderBook.handle(buyOrder);
            assertEquals(1, orderBook.ordersToBuy.size());
            assertEquals(0, orderBook.ordersToSell.size());

            Order sellOrder = new Order("SELL,2,200,10");
            orderBook.handle(sellOrder);
            assertEquals(1, orderBook.ordersToBuy.size());
            assertEquals(1, orderBook.ordersToSell.size());
        } catch (OrderException e) {
            fail("Unexpected OrderException: " + e.getMessage());
        }
    }

    /**
     * should handle a new buy order and add it to the ordersToBuy list
     */
    @Test
    public void handleNewBuyOrder() {
        orderBook.handle(new Order("BUY,1,100,10"));
        assertEquals(1, orderBook.ordersToBuy.size());
        assertEquals(0, orderBook.ordersToSell.size());
        assertEquals(1, orderBook.ordersMap.size());
    }

    /**
     * should not execute orders when there is no matching buy and sell order
     */
    @Test
    public void handleNonMatchingBuyAndSellOrders() throws OrderException {
        orderBook.handle(new Order("BUY,1,100,10"));
        orderBook.handle(new Order("SELL,2,200,10"));
        assertEquals(1, orderBook.ordersToBuy.size());
        assertEquals(1, orderBook.ordersToSell.size());
    }

    /**
     * should handle a new sell order and add it to the ordersToSell list
     */
    @Test
    public void handleNewSellOrder() throws OrderException {
        orderBook.handle(new Order("SELL,1,100,10"));
        assertEquals(1, orderBook.ordersToSell.size());
        assertEquals(0, orderBook.ordersToBuy.size());
        assertEquals(1, orderBook.ordersMap.size());
    }

    /**
     * should execute orders when there is a matching buy and sell order
     */
    @Test
    public void handleMatchingBuyAndSellOrders() throws OrderException {
        orderBook.handle(new Order("BUY,1,100,10"));
        orderBook.handle(new Order("SELL,2,100,10"));
        assertEquals(0, orderBook.getOrdersToBuy().size());
        assertEquals(0, orderBook.getOrdersToSell().size());
    }

    /**
     * should return the correct comparison function for sell orders
     */
    @Test
    public void getOrderComparisonForSellOrders() throws OrderException {
        Order order = new Order("SELL,1,100,10");
        OrderComparisonFunction comparison =
                (OrderComparisonFunction)
                        ReflectionTestUtils.invokeMethod(orderBook, "getOrderComaprison", order);
        assertEquals(comparison, orderBook.lLessEqual);
    }

    /**
     * should return the correct comparison function for buy orders
     */
    @Test
    public void getOrderComparisonForBuyOrders() throws OrderException  {
        Order order = new Order("BUY,1,1,1");
        OrderComparisonFunction comparison =
                (OrderComparisonFunction)
                        ReflectionTestUtils.invokeMethod(orderBook, "getOrderComaprison", order);
        assertTrue(comparison.run(new Order("BUY,1,1,1"), new Order("BUY,1,1,1")));
        assertFalse(comparison.run(new Order("BUY,1,2,1"), new Order("BUY,1,1,1")));
    }

    @Test
    public void testHandleNewOrder() {
        Order order = new Order(1, true, 10, 100);
        orderBook.handle(order);
        assertEquals(1, orderBook.ordersMap.size());
        assertEquals(order, orderBook.ordersMap.get(1));
    }

    @Test
    public void testHandleExistingOrder() {
        Order order1 = new Order(1, true, 10, 100);
        Order order2 = new Order(2, false, 10, 100);
        orderBook.handle(order1);
        orderBook.handle(order2);
        assertEquals(0, orderBook.ordersMap.size());
    }

    @Test
    public void testGetOrderIdsAvailableToLock() throws OrderNotHandledException {
        Order order1 = new Order(1, true, 10, 100);
        Order order2 = new Order(2, true, 9, 100);
        Order order3 = new Order(3, true, 11, 100);
        orderBook.handle(order1);
        orderBook.handle(order2);
        orderBook.handle(order3);
        assertEquals(2, orderBook.getOrderIdsAvailableToLock(order1, orderBook.ordersToSell, orderBook.lLessEqual).size());
        assertEquals(1, orderBook.getOrderIdsAvailableToLock(order1, orderBook.ordersToSell, orderBook.lGreaterEqual).size());
    }

    @Test
    public void testLockToAllocate() throws OrderNotHandledException {
        Order order1 = new Order(1, true, 10, 100);
        Order order2 = new Order(2, true, 9, 100);
        Order order3 = new Order(3, true, 11, 100);
        orderBook.handle(order1);
        orderBook.handle(order2);
        orderBook.handle(order3);
        assertEquals(2, orderBook.lockToAllocate(orderBook.mutexToSell, orderBook.lockedIdsToSell, orderBook.getOrderIdsAvailableToLock(order1, orderBook.ordersToSell, orderBook.lLessEqual), 100).size());
        assertEquals(1, orderBook.lockToAllocate(orderBook.mutexToSell, orderBook.lockedIdsToSell, orderBook.getOrderIdsAvailableToLock(order1, orderBook.ordersToSell, orderBook.lGreaterEqual), 100).size());
    }
}