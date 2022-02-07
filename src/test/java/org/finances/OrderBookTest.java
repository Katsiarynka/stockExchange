package org.finances;

public class OrderBookTest {

    @Test
    void getOrdersToSell_whenSuccess_thenReturnOrdersToSell() {
        OrderBook orderBook = new OrderBook();
        orderBook.handle(new Order(1, true, 1));
        orderBook.handle(new Order(2, true, 2));
        orderBook.handle(new Order(3, true, 3));
        orderBook.handle(new Order(4, false, 4));
        orderBook.handle(new Order(5, false, 5));
        orderBook.handle(new Order(6, false, 6));

        assertEquals("[1, 2, 3]", orderBook.getOrdersToSell());
    }

    @Test
    void getOrdersToSell_whenNull_thenReturnEmptyString() {
        OrderBook orderBook = new OrderBook();
        assertEquals("", orderBook.getOrdersToSell());
    }
}