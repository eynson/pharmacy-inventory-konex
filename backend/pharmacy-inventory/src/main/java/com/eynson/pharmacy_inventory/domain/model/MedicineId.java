package com.eynson.pharmacy_inventory.domain.model;

import java.util.UUID;

public class MedicineId {
    private final String value;

    private MedicineId(String value) {
        this.value = value;
    }

    public static MedicineId create() {
        return new MedicineId(UUID.randomUUID().toString());
    }

    public static MedicineId from(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("MedicineId no puede ser nulo o vacío");
        }
        return new MedicineId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineId that = (MedicineId) o;
        return value.equals(that.value);
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
