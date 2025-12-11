package com.eynson.pharmacy_inventory.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MedicineId Value Object Tests")
class MedicineIdTest {

    @Test
    @DisplayName("should create MedicineId with valid UUID")
    void shouldCreateWithValidUUID() {
        var medicineId = MedicineId.create();
        assertNotNull(medicineId);
        assertNotNull(medicineId.getValue());
    }

    @Test
    @DisplayName("should reconstruct MedicineId from string")
    void shouldReconstructFromString() {
        String id = "550e8400-e29b-41d4-a716-446655440000";
        var medicineId = MedicineId.from(id);
        assertEquals(id, medicineId.getValue());
    }

    @Test
    @DisplayName("should throw exception for null UUID")
    void shouldThrowExceptionForNull() {
        assertThrows(IllegalArgumentException.class, () -> MedicineId.from(null));
    }

    @Test
    @DisplayName("should throw exception for empty UUID")
    void shouldThrowExceptionForEmpty() {
        assertThrows(IllegalArgumentException.class, () -> MedicineId.from(""));
    }

    @Test
    @DisplayName("should create two different MedicineIds")
    void shouldCreateDifferentIds() {
        var id1 = MedicineId.create();
        var id2 = MedicineId.create();
        assertNotEquals(id1.getValue(), id2.getValue());
    }
}
