package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.exception.InvalidMedicineDataException;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateMedicineUseCaseImpl implements CreateMedicineUseCase {
    private final MedicineRepositoryPort medicineRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CreateMedicineUseCaseImpl(MedicineRepositoryPort medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine execute(CreateMedicineCommand command) {
        try {
            // Validar datos
            validateCommand(command);

            // Verificar que no exista una medicina con el mismo nombre
            if (medicineRepository.findByName(command.name().trim()).isPresent()) {
                throw new IllegalArgumentException("Ya existe una medicina con el nombre '" + command.name().trim() + "'");
            }

            // Parsear fechas
            LocalDateTime manufacturingDate = parseDate(command.manufacturingDate());
            LocalDateTime expirationDate = parseDate(command.expirationDate());

            // Crear el valor monetario
            Money unitValue = Money.from(command.unitValue());

            // Crear la medicina (el constructor realiza validaciones de negocio)
            Medicine medicine = Medicine.create(
                    command.name().trim(),
                    command.factoryLaboratory().trim(),
                    manufacturingDate,
                    expirationDate,
                    command.quantityInStock(),
                    unitValue
            );

            // Guardar en el repositorio
            return medicineRepository.save(medicine);
        } catch (IllegalArgumentException e) {
            throw new InvalidMedicineDataException(e.getMessage(), e);
        }
    }

    private void validateCommand(CreateMedicineCommand command) {
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
