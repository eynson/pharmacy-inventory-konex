package com.eynson.pharmacy_inventory.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final int SCALE = 2;

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money from(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        if (amount.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        return new Money(amount.setScale(SCALE, java.math.RoundingMode.HALF_UP));
    }

    public static Money from(String amount) {
        try {
            return from(new BigDecimal(amount));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de monto inválido: " + amount, e);
        }
    }

    public static Money zero() {
        return new Money(ZERO.setScale(SCALE, java.math.RoundingMode.HALF_UP));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Money multiply(int quantity) {
        return new Money(amount.multiply(BigDecimal.valueOf(quantity))
                .setScale(SCALE, java.math.RoundingMode.HALF_UP));
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "No se puede sumar dinero nulo");
        return new Money(amount.add(other.amount)
                .setScale(SCALE, java.math.RoundingMode.HALF_UP));
    }

    public boolean isPositive() {
        return amount.compareTo(ZERO) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return "$" + amount;
    }
}
