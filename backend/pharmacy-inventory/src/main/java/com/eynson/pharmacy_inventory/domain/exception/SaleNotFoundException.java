package com.eynson.pharmacy_inventory.domain.exception;

public class SaleNotFoundException extends DomainException {
    public SaleNotFoundException(String message) {
        super(message, "SALE_NOT_FOUND");
    }

    public SaleNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
