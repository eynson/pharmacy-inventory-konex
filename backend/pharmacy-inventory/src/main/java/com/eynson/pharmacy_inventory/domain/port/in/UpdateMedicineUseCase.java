package com.eynson.pharmacy_inventory.domain.port.in;

import com.eynson.pharmacy_inventory.domain.model.Medicine;

public interface UpdateMedicineUseCase {
    Medicine execute(UpdateMedicineCommand command);

    record UpdateMedicineCommand(
            String id,
            String name,
            String factoryLaboratory,
            String manufacturingDate,
            String expirationDate,
            Integer quantityInStock,
            String unitValue
    ) {}
}
