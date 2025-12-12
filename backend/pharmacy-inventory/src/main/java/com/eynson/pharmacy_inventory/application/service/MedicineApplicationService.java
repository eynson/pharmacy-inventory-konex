package com.eynson.pharmacy_inventory.application.service;

import com.eynson.pharmacy_inventory.application.dto.request.CreateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.request.GetMedicinesRequest;
import com.eynson.pharmacy_inventory.application.dto.request.UpdateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.response.MedicineResponse;
import com.eynson.pharmacy_inventory.application.dto.response.PagedMedicineResponse;
import com.eynson.pharmacy_inventory.application.mapper.MedicineMapper;
import com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.DeleteMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.GetMedicineByIdUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.GetMedicinesUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.UpdateMedicineUseCase;
import org.springframework.stereotype.Service;

@Service
public class MedicineApplicationService {
    private final CreateMedicineUseCase createMedicineUseCase;
    private final UpdateMedicineUseCase updateMedicineUseCase;
    private final DeleteMedicineUseCase deleteMedicineUseCase;
    private final GetMedicineByIdUseCase getMedicineByIdUseCase;
    private final GetMedicinesUseCase getMedicinesUseCase;
    private final MedicineMapper medicineMapper;

    public MedicineApplicationService(
            CreateMedicineUseCase createMedicineUseCase,
            UpdateMedicineUseCase updateMedicineUseCase,
            DeleteMedicineUseCase deleteMedicineUseCase,
            GetMedicineByIdUseCase getMedicineByIdUseCase,
            GetMedicinesUseCase getMedicinesUseCase,
            MedicineMapper medicineMapper) {
        this.createMedicineUseCase = createMedicineUseCase;
        this.updateMedicineUseCase = updateMedicineUseCase;
        this.deleteMedicineUseCase = deleteMedicineUseCase;
        this.getMedicineByIdUseCase = getMedicineByIdUseCase;
        this.getMedicinesUseCase = getMedicinesUseCase;
        this.medicineMapper = medicineMapper;
    }

    public MedicineResponse createMedicine(CreateMedicineRequest request) {
        var command = medicineMapper.toCreateCommand(request);
        var medicine = createMedicineUseCase.execute(command);
        return medicineMapper.toResponse(medicine);
    }

    public MedicineResponse updateMedicine(UpdateMedicineRequest request) {
        var command = medicineMapper.toUpdateCommand(request);
        var medicine = updateMedicineUseCase.execute(command);
        return medicineMapper.toResponse(medicine);
    }

    public void deleteMedicine(String medicineId) {
        deleteMedicineUseCase.execute(medicineId);
    }

    public MedicineResponse getMedicineById(String medicineId) {
        var medicine = getMedicineByIdUseCase.execute(medicineId);
        return medicineMapper.toResponse(medicine);
    }

    public PagedMedicineResponse getMedicines(GetMedicinesRequest request) {
        var query = new GetMedicinesUseCase.GetMedicinesQuery(
                request.page(),
                request.pageSize(),
                request.search(),
                "name"
        );
        var result = getMedicinesUseCase.execute(query);
        var medicineResponses = result.content().stream()
                .map(medicineMapper::toResponse)
                .toList();
        return new PagedMedicineResponse(
                medicineResponses,
                result.currentPage(),
                result.pageSize(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
