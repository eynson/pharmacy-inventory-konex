package com.eynson.pharmacy_inventory.domain.port.in;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import java.util.List;

public interface GetMedicinesUseCase {
    PagedResult<Medicine> execute(GetMedicinesQuery query);

    record GetMedicinesQuery(
            Integer page,
            Integer pageSize,
            String search,
            String sortBy
    ) {}

    record PagedResult<T>(
            List<T> content,
            Integer totalPages,
            Long totalElements,
            Integer currentPage,
            Integer pageSize
    ) {}
}
