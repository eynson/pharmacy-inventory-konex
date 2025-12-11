package com.eynson.pharmacy_inventory.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Quantity Value Object Tests")
class QuantityTest {

    @Test
    @DisplayName("should create Quantity with valid amount")
    void shouldCreateWithValidAmount() {
        var quantity = Quantity.from(100);
        assertEquals(100, quantity.getValue());
    }

    @Test
    @DisplayName("should throw exception for null quantity")
    void shouldThrowExceptionForNull() {
        assertThrows(IllegalArgumentException.class, () -> Quantity.from(null));
    }

    @Test
    @DisplayName("should allow zero quantity")
    void shouldAllowZeroQuantity() {
        var quantity = Quantity.from(0);
        assertEquals(0, quantity.getValue());
    }

    @Test
    @DisplayName("should throw exception for negative quantity")
    void shouldThrowExceptionForNegative() {
        assertThrows(IllegalArgumentException.class, () -> Quantity.from(-5));
    }

    @Test
    @DisplayName("should check if quantity is available")
    void shouldCheckAvailability() {
        var quantity = Quantity.from(100);
        assertTrue(quantity.isAvailable(50));
        assertFalse(quantity.isAvailable(150));
    }

    @Test
    @DisplayName("should subtract quantity")
    void shouldSubtractQuantity() {
        var quantity = Quantity.from(100);
        var result = quantity.subtract(30);
        assertEquals(70, result.getValue());
    }
}
