package com.eynson.pharmacy_inventory.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MedicineResponse(
        String id,
        String name,
        String factoryLaboratory,
        LocalDateTime manufacturingDate,
        LocalDateTime expirationDate,
        Integer quantityInStock,
        BigDecimal unitValue,
        Boolean isExpired
) {
}
