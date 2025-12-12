package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.exception.InvalidMedicineDataException;
import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.port.in.UpdateMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import java.time.LocalDateTime;

public class UpdateMedicineUseCaseImpl implements UpdateMedicineUseCase {
    private final MedicineRepositoryPort medicineRepository;

    public UpdateMedicineUseCaseImpl(MedicineRepositoryPort medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine execute(UpdateMedicineCommand command) {
        try {
            // Validar que el ID es requerido
            if (command.id() == null || command.id().trim().isEmpty()) {
                throw new IllegalArgumentException("El ID del medicamento es requerido");
            }

            // Buscar medicina existente
            MedicineId medicineId = MedicineId.from(command.id());
            Medicine medicine = medicineRepository.findById(medicineId)
                    .orElseThrow(() -> new MedicineNotFoundException(command.id()));

            // Usar valores existentes si no se proporcionan nuevos valores
            String name = (command.name() != null && !command.name().trim().isEmpty()) ? command.name().trim() : medicine.getName();
            String factoryLaboratory = (command.factoryLaboratory() != null && !command.factoryLaboratory().trim().isEmpty()) ? command.factoryLaboratory().trim() : medicine.getFactoryLaboratory();
            LocalDateTime manufacturingDate = (command.manufacturingDate() != null && !command.manufacturingDate().trim().isEmpty()) ? parseDate(command.manufacturingDate()) : medicine.getManufacturingDate();
            LocalDateTime expirationDate = (command.expirationDate() != null && !command.expirationDate().trim().isEmpty()) ? parseDate(command.expirationDate()) : medicine.getExpirationDate();
            Integer quantityInStock = (command.quantityInStock() != null) ? command.quantityInStock() : medicine.getQuantityInStock().getValue();
            Money unitValue = (command.unitValue() != null && !command.unitValue().trim().isEmpty()) ? Money.from(command.unitValue()) : medicine.getUnitValue();

            // Validar datos requeridos
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("El nombre del medicamento es requerido");
            }
            if (factoryLaboratory == null || factoryLaboratory.isEmpty()) {
                throw new IllegalArgumentException("El laboratorio de fabricación es requerido");
            }
            if (manufacturingDate == null) {
                throw new IllegalArgumentException("La fecha de fabricación es requerida");
            }
            if (expirationDate == null) {
                throw new IllegalArgumentException("La fecha de vencimiento es requerida");
            }
            if (quantityInStock < 0) {
                throw new IllegalArgumentException("La cantidad en stock debe ser mayor o igual a 0");
            }

            // Reconstruir medicina con nuevos datos
            Medicine updatedMedicine = Medicine.reconstruct(
                    medicineId,
                    name,
                    factoryLaboratory,
                    manufacturingDate,
                    expirationDate,
                    quantityInStock,
                    unitValue,
                    medicine.getCreatedAt(),
                    LocalDateTime.now()
            );

            // Guardar cambios
            return medicineRepository.save(updatedMedicine);
        } catch (IllegalArgumentException e) {
            throw new InvalidMedicineDataException(e.getMessage(), e);
        }
    }

    private void validateCommand(UpdateMedicineCommand command) {
        if (command.id() == null || command.id().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del medicamento es requerido");
        }
        if (command.name() == null || command.name().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento es requerido");
        }
        if (command.factoryLaboratory() == null || command.factoryLaboratory().trim().isEmpty()) {
            throw new IllegalArgumentException("El laboratorio de fabricación es requerido");
        }
        if (command.manufacturingDate() == null || command.manufacturingDate().trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de fabricación es requerida");
        }
        if (command.expirationDate() == null || command.expirationDate().trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de vencimiento es requerida");
        }
        if (command.quantityInStock() == null || command.quantityInStock() < 0) {
            throw new IllegalArgumentException("La cantidad en stock debe ser mayor o igual a 0");
        }
        if (command.unitValue() == null || command.unitValue().trim().isEmpty()) {
            throw new IllegalArgumentException("El valor unitario es requerido");
        }
    }

    private LocalDateTime parseDate(String dateString) {
        try {
            return LocalDateTime.parse(dateString + "T00:00:00");
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd", e);
        }
    }
}
