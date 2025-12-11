package com.eynson.pharmacy_inventory.domain.port.in;

import com.eynson.pharmacy_inventory.domain.model.Medicine;

public interface CreateMedicineUseCase {
    Medicine execute(CreateMedicineCommand command);

    record CreateMedicineCommand(
            String name,
            String factoryLaboratory,
            String manufacturingDate,
            String expirationDate,
            Integer quantityInStock,
            String unitValue
    ) {}
}
