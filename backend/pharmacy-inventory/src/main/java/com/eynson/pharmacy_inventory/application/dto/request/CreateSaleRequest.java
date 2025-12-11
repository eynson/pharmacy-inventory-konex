package com.eynson.pharmacy_inventory.application.dto.request;

public record CreateSaleRequest(
        String medicineId,
        Integer quantitySold
) {
}
