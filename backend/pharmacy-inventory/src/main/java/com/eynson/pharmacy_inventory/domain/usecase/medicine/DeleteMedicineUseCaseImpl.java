package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.port.in.DeleteMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;

public class DeleteMedicineUseCaseImpl implements DeleteMedicineUseCase {
    private final MedicineRepositoryPort medicineRepository;

    public DeleteMedicineUseCaseImpl(MedicineRepositoryPort medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public void execute(String medicineId) {
        if (medicineId == null || medicineId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del medicamento es requerido");
        }

        MedicineId id = MedicineId.from(medicineId);

        // Verificar que existe antes de intentar eliminar
        if (!medicineRepository.existsById(id)) {
            throw new MedicineNotFoundException(medicineId);
        }

        medicineRepository.deleteById(id);
    }
}
