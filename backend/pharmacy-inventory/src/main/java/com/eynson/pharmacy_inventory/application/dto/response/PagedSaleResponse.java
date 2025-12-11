package com.eynson.pharmacy_inventory.application.dto.response;

import java.util.List;

public record PagedSaleResponse(
        List<SaleResponse> content,
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages
) {
}
