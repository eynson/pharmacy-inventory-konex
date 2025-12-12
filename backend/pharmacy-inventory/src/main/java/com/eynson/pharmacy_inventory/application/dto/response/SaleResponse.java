package com.eynson.pharmacy_inventory.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponse(
        String id,
        String medicineId,
        String medicineName,
        Integer quantitySold,
        BigDecimal unitValue,
        BigDecimal totalValue,
        LocalDateTime saleDate
) {
}
