package com.eynson.pharmacy_inventory.domain.port.in;

import com.eynson.pharmacy_inventory.domain.model.Medicine;

public interface GetMedicineByIdUseCase {
    Medicine execute(String medicineId);
}
