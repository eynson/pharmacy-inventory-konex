package com.eynson.pharmacy_inventory.application.mapper;

import com.eynson.pharmacy_inventory.application.dto.request.CreateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.request.UpdateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.response.MedicineResponse;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.UpdateMedicineUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

    @Mapping(target = "id", ignore = true)
    MedicineResponse toResponse(Medicine medicine);

    CreateMedicineUseCase.CreateMedicineCommand toCreateCommand(CreateMedicineRequest request);

    UpdateMedicineUseCase.UpdateMedicineCommand toUpdateCommand(UpdateMedicineRequest request);
}
