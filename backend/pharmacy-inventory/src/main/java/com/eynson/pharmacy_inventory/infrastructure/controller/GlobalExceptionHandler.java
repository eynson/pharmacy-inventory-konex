package com.eynson.pharmacy_inventory.infrastructure.controller;

import com.eynson.pharmacy_inventory.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicineNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMedicineNotFound(MedicineNotFoundException ex) {
        var body = createErrorResponse(ex.getMessage(), "MEDICINE_NOT_FOUND", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStock(InsufficientStockException ex) {
        var body = createErrorResponse(ex.getMessage(), "INSUFFICIENT_STOCK", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidMedicineDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidMedicineData(InvalidMedicineDataException ex) {
        var body = createErrorResponse(ex.getMessage(), "INVALID_MEDICINE_DATA", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(DomainException ex) {
        var body = createErrorResponse(ex.getMessage(), "DOMAIN_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        var body = createErrorResponse("Error interno del servidor", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, Object> createErrorResponse(String message, String errorCode, int status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", errorCode);
        body.put("message", message);
        return body;
    }
}
