package com.eynson.pharmacy_inventory.application.dto.request;

public record GetSalesByDateRangeRequest(
        String startDate,
        String endDate,
        Integer page,
        Integer pageSize
) {
}
