package com.eynson.pharmacy_inventory.domain.model;

public class Quantity {
    private final Integer value;

    private Quantity(Integer value) {
        this.value = value;
    }

    public static Quantity from(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula");
        }
        if (value < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        return new Quantity(value);
    }

    public static Quantity zero() {
        return new Quantity(0);
    }

    public Integer getValue() {
        return value;
    }

    public boolean isAvailable(Integer requiredQuantity) {
        return value >= requiredQuantity;
    }

    public Quantity subtract(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("No se puede restar una cantidad negativa");
        }
        if (amount > value) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        return new Quantity(value - amount);
    }

    public Quantity add(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("No se puede sumar una cantidad negativa");
        }
        return new Quantity(value + amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value.equals(quantity.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
