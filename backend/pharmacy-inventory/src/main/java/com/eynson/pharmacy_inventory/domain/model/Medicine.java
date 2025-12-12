package com.eynson.pharmacy_inventory.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Medicine {
    private final MedicineId id;
    private final String name;
    private final String factoryLaboratory;
    private final LocalDateTime manufacturingDate;
    private final LocalDateTime expirationDate;
    private Quantity quantityInStock;
    private final Money unitValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Medicine(
            MedicineId id,
            String name,
            String factoryLaboratory,
            LocalDateTime manufacturingDate,
            LocalDateTime expirationDate,
            Quantity quantityInStock,
            Money unitValue) {
        validateBasicData(name, factoryLaboratory);
        validateDates(manufacturingDate, expirationDate);

        this.id = id;
        this.name = name;
        this.factoryLaboratory = factoryLaboratory;
        this.manufacturingDate = manufacturingDate;
        this.expirationDate = expirationDate;
        this.quantityInStock = quantityInStock;
        this.unitValue = unitValue;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Medicine create(
            String name,
            String factoryLaboratory,
            LocalDateTime manufacturingDate,
            LocalDateTime expirationDate,
            Integer quantityInStock,
            Money unitValue) {
        return new Medicine(
                MedicineId.create(),
                name,
                factoryLaboratory,
                manufacturingDate,
                expirationDate,
                Quantity.from(quantityInStock),
                unitValue
        );
    }

    public static Medicine reconstruct(MedicineReconstructionData data) {
        Medicine medicine = new Medicine(
                data.id(),
                data.name(),
                data.factoryLaboratory(),
                data.manufacturingDate(),
                data.expirationDate(),
                Quantity.from(data.quantityInStock()),
                data.unitValue()
        );
        medicine.createdAt = data.createdAt();
        medicine.updatedAt = data.updatedAt();
        return medicine;
    }

    public record MedicineReconstructionData(
            MedicineId id,
            String name,
            String factoryLaboratory,
            LocalDateTime manufacturingDate,
            LocalDateTime expirationDate,
            Integer quantityInStock,
            Money unitValue,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    private void validateBasicData(String name, String factoryLaboratory) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la medicina no puede estar vacío");
        }
        if (factoryLaboratory == null || factoryLaboratory.trim().isEmpty()) {
            throw new IllegalArgumentException("El laboratorio de la medicina no puede estar vacío");
        }
    }

    private void validateDates(LocalDateTime manufacturingDate, LocalDateTime expirationDate) {
        if (manufacturingDate == null) {
            throw new IllegalArgumentException("La fecha de fabricación no puede ser nula");
        }
        if (expirationDate == null) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
        }
        if (expirationDate.isBefore(manufacturingDate)) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento no puede ser anterior a la fecha de fabricación"
            );
        }
    }

    public void updateStock(Integer quantity) {
        this.quantityInStock = Quantity.from(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public void sellUnits(Integer quantity) {
        if (!quantityInStock.isAvailable(quantity)) {
            throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: " + quantityInStock.getValue()
            );
        }
        this.quantityInStock = quantityInStock.subtract(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public Money calculateTotalPrice(Integer quantity) {
        return unitValue.multiply(quantity);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public boolean isExpiredOn(LocalDateTime dateTime) {
        return dateTime.isAfter(expirationDate);
    }

    public MedicineId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFactoryLaboratory() {
        return factoryLaboratory;
    }

    public LocalDateTime getManufacturingDate() {
        return manufacturingDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public Quantity getQuantityInStock() {
        return quantityInStock;
    }

    public Money getUnitValue() {
        return unitValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return Objects.equals(id, medicine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", factoryLaboratory='" + factoryLaboratory + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", unitValue=" + unitValue +
                ", isExpired=" + isExpired() +
                '}';
    }
}
