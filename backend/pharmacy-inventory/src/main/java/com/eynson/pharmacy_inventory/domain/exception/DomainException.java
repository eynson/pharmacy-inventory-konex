package com.eynson.pharmacy_inventory.domain.exception;

public class DomainException extends RuntimeException {
    private final String errorCode;

    public DomainException(String message) {
        this(message, null, null);
    }

    public DomainException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public DomainException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public DomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
