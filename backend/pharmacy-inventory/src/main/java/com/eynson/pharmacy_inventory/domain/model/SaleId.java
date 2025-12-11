package com.eynson.pharmacy_inventory.domain.model;

import java.util.UUID;

public class SaleId {
    private final String value;

    private SaleId(String value) {
        this.value = value;
    }

    public static SaleId create() {
        return new SaleId(UUID.randomUUID().toString());
    }

    public static SaleId from(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("SaleId no puede ser nulo o vacío");
        }
        return new SaleId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleId saleId = (SaleId) o;
        return value.equals(saleId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
