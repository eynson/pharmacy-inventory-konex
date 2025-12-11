package com.eynson.pharmacy_inventory.domain.exception;

public class MedicineNotFoundException extends DomainException {
    public MedicineNotFoundException(String medicineId) {
        super("Medicina no encontrada: " + medicineId, "MEDICINE_NOT_FOUND");
    }

    public MedicineNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
