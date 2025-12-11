package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.port.in.GetMedicineByIdUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;

public class GetMedicineByIdUseCaseImpl implements GetMedicineByIdUseCase {
    private final MedicineRepositoryPort medicineRepository;

    public GetMedicineByIdUseCaseImpl(MedicineRepositoryPort medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine execute(String medicineId) {
        if (medicineId == null || medicineId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del medicamento es requerido");
        }

        MedicineId id = MedicineId.from(medicineId);
        return medicineRepository.findById(id)
                .orElseThrow(() -> new MedicineNotFoundException(medicineId));
    }
}
