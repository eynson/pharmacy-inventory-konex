package com.eynson.pharmacy_inventory.application.dto.response;

import java.util.List;

public record PagedMedicineResponse(
        List<MedicineResponse> content,
        Integer page,
        Integer pageSize,
        Long totalElements,
        Integer totalPages
) {
}
