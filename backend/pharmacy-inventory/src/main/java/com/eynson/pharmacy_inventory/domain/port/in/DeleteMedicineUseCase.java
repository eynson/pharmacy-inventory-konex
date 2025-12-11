package com.eynson.pharmacy_inventory.domain.port.in;

public interface DeleteMedicineUseCase {
    void execute(String medicineId);
}
