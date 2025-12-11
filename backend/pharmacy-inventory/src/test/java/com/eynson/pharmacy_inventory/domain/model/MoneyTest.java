package com.eynson.pharmacy_inventory.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money Value Object Tests")
class MoneyTest {

    @Test
    @DisplayName("should create Money with valid amount")
    void shouldCreateWithValidAmount() {
        var money = Money.from(new BigDecimal("100.50"));
        assertEquals(new BigDecimal("100.50"), money.getAmount());
    }

    @Test
    @DisplayName("should throw exception for null amount")
    void shouldThrowExceptionForNull() {
        assertThrows(IllegalArgumentException.class, () -> Money.from((BigDecimal) null));
    }

    @Test
    @DisplayName("should throw exception for negative amount")
    void shouldThrowExceptionForNegative() {
        assertThrows(IllegalArgumentException.class, () -> Money.from(new BigDecimal("-10.00")));
    }

    @Test
    @DisplayName("should allow zero amount")
    void shouldAllowZeroAmount() {
        var money = Money.from(BigDecimal.ZERO);
        assertEquals(new BigDecimal("0.00"), money.getAmount());
    }

    @Test
    @DisplayName("should add two Money objects")
    void shouldAddMoney() {
        var money1 = Money.from(new BigDecimal("100.00"));
        var money2 = Money.from(new BigDecimal("50.00"));
        var result = money1.add(money2);
        assertEquals(new BigDecimal("150.00"), result.getAmount());
    }

    @Test
    @DisplayName("should multiply Money by quantity")
    void shouldMultiplyMoney() {
        var money = Money.from(new BigDecimal("100.00"));
        var result = money.multiply(5);
        assertEquals(new BigDecimal("500.00"), result.getAmount());
    }
}
