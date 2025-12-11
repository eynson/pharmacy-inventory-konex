package com.eynson.pharmacy_inventory.domain.exception;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException(Integer required, Integer available) {
        super(
                String.format("Stock insuficiente. Requerido: %d, Disponible: %d", required, available),
                "INSUFFICIENT_STOCK"
        );
    }

    public InsufficientStockException(String message) {
        super(message, "INSUFFICIENT_STOCK");
    }
}
