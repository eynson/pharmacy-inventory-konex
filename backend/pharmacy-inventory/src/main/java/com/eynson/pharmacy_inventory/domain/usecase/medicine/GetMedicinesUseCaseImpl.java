package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.port.in.GetMedicinesUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;

public class GetMedicinesUseCaseImpl implements GetMedicinesUseCase {
    private final MedicineRepositoryPort medicineRepository;

    public GetMedicinesUseCaseImpl(MedicineRepositoryPort medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public PagedResult<Medicine> execute(GetMedicinesQuery query) {
        validateQuery(query);

        var result = medicineRepository.findAll(
                query.page(),
                query.pageSize(),
                query.search(),
                query.sortBy()
        );

        return new PagedResult<>(
                result.content(),
                result.totalPages(),
                result.totalElements(),
                result.currentPage(),
                result.pageSize()
        );
    }

    private void validateQuery(GetMedicinesQuery query) {
        if (query.page() == null || query.page() < 0) {
            throw new IllegalArgumentException("El número de página debe ser mayor o igual a 0");
        }
        if (query.pageSize() == null || query.pageSize() <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor a 0");
        }
    }
}
