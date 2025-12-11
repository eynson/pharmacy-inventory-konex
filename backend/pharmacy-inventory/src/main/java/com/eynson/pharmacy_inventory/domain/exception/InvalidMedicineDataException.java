package com.eynson.pharmacy_inventory.domain.exception;

public class InvalidMedicineDataException extends DomainException {
    public InvalidMedicineDataException(String message) {
        super(message, "INVALID_MEDICINE_DATA");
    }

    public InvalidMedicineDataException(String message, Throwable cause) {
        super(message, "INVALID_MEDICINE_DATA", cause);
    }
}
