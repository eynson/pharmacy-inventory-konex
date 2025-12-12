package com.eynson.pharmacy_inventory.application.mapper;

import com.eynson.pharmacy_inventory.application.dto.request.CreateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.request.UpdateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.response.MedicineResponse;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.UpdateMedicineUseCase;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MedicineMapper {

    default MedicineResponse toResponse(Medicine medicine) {
        return new MedicineResponse(
                medicine.getId().getValue(),
                medicine.getName(),
                medicine.getFactoryLaboratory(),
                medicine.getManufacturingDate(),
                medicine.getExpirationDate(),
                medicine.getQuantityInStock().getValue(),
                medicine.getUnitValue().getAmount(),
                medicine.isExpired()
        );
    }

    default CreateMedicineUseCase.CreateMedicineCommand toCreateCommand(CreateMedicineRequest request) {
        return new CreateMedicineUseCase.CreateMedicineCommand(
                request.name(),
                request.factoryLaboratory(),
                request.manufacturingDate(),
                request.expirationDate(),
                request.quantityInStock(),
                request.unitValue().toPlainString()
        );
    }

    default UpdateMedicineUseCase.UpdateMedicineCommand toUpdateCommand(UpdateMedicineRequest request) {
        return new UpdateMedicineUseCase.UpdateMedicineCommand(
                request.id(),
                request.name(),
                request.factoryLaboratory(),
                request.manufacturingDate(),
                request.expirationDate(),
                request.quantityInStock(),
                request.unitValue().toPlainString()
        );
    }
}
