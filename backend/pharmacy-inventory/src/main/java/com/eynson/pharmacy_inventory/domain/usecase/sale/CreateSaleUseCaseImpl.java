package com.eynson.pharmacy_inventory.domain.usecase.sale;

import com.eynson.pharmacy_inventory.domain.exception.InsufficientStockException;
import com.eynson.pharmacy_inventory.domain.exception.InvalidMedicineDataException;
import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.port.in.CreateSaleUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;

public class CreateSaleUseCaseImpl implements CreateSaleUseCase {
    private final MedicineRepositoryPort medicineRepository;
    private final SaleRepositoryPort saleRepository;

    public CreateSaleUseCaseImpl(
            MedicineRepositoryPort medicineRepository,
            SaleRepositoryPort saleRepository) {
        this.medicineRepository = medicineRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale execute(CreateSaleCommand command) {
        try {
            // Validar datos
            validateCommand(command);

            // Obtener la medicina
            MedicineId medicineId = MedicineId.from(command.medicineId());
            Medicine medicine = medicineRepository.findById(medicineId)
                    .orElseThrow(() -> new MedicineNotFoundException(command.medicineId()));

            // Validar que hay stock suficiente
            if (!medicine.getQuantityInStock().isAvailable(command.quantity())) {
                throw new InsufficientStockException(
                        command.quantity(),
                        medicine.getQuantityInStock().getValue()
                );
            }

            // Crear la venta
            Sale sale = Sale.create(
                    medicineId,
                    medicine.getName(),
                    command.quantity(),
                    medicine.getUnitValue()
            );

            // Actualizar el stock de la medicina
            medicine.sellUnits(command.quantity());
            medicineRepository.save(medicine);

            // Guardar la venta
            return saleRepository.save(sale);
        } catch (IllegalArgumentException e) {
            throw new InvalidMedicineDataException(e.getMessage(), e);
        }
    }

    private void validateCommand(CreateSaleCommand command) {
        if (command.medicineId() == null || command.medicineId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del medicamento es requerido");
        }
        if (command.quantity() == null || command.quantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
    }
}
