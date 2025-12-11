package com.eynson.pharmacy_inventory.application.dto.request;

import java.math.BigDecimal;

public record UpdateMedicineRequest(
        String id,
        String name,
        String factoryLaboratory,
        String manufacturingDate,
        String expirationDate,
        Integer quantityInStock,
        BigDecimal unitValue
) {
}
