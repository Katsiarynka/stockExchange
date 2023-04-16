package org.finances;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("Should return false when the input string has incorrect number of values")
    void isValidWhenInputStringHasIncorrectNumberOfValues() {
        assertFalse(Order.isValid("BUY,1,100"));
        assertFalse(Order.isValid("BUY,1,100,50,30,20"));
    }

    @Test
    @DisplayName("Should return true when the input string is valid")
    void isValidWhenInputStringIsValid() {
        String validOrderString = "BUY,1,100,10";
        assertTrue(
                Order.isValid(validOrderString),
                "Expected isValid to return true for a valid input string");
    }

    @Test
    @DisplayName("Should return false when the input string contains non-integer values")
    void isValidWhenInputStringContainsNonIntegerValues() {
        String invalidOrderString = "BUY,1,2A,3";
        assertFalse(
                Order.isValid(invalidOrderString),
                "Expected isValid to return false for non-integer values");
    }

    @Test
    @DisplayName(
            "Should return false when the input string contains negative or zero integer values")
    void isValidWhenInputStringContainsNegativeOrZeroIntegerValues() {
        assertFalse(Order.isValid("BUY,-1,100,50"));
        assertFalse(Order.isValid("BUY,1,-100,50"));
        assertFalse(Order.isValid("BUY,1,100,-50"));
        assertFalse(Order.isValid("BUY,1,100,50,-10"));
    }

    @Test
    @DisplayName(
            "Should return false when the input string contains integer values less than or equal to 0")
    void isValidWhenInputStringContainsIntegerValuesLessThanOrEqualToZero() {
        assertFalse(Order.isValid("BUY,-1,100,50"));
        assertFalse(Order.isValid("BUY,1,-100,50"));
        assertFalse(Order.isValid("BUY,1,100,-50"));
        assertFalse(Order.isValid("BUY,1,100,50,-10"));
    }

    @Test
    @DisplayName("should return the correct price of the order")
    void getPriceReturnsCorrectPrice() throws OrderException {
        Order order = new Order("BUY,1,100,10");
        Short price = order.getPrice();
        assertEquals(100, price.intValue());
    }
}