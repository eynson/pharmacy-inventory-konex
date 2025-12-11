package com.eynson.pharmacy_inventory.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Sale {
    private final SaleId id;
    private final MedicineId medicineId;
    private final String medicineName;
    private final Quantity quantitySold;
    private final Money unitValue;
    private final Money totalValue;
    private final LocalDateTime saleDateTime;
    private final LocalDateTime createdAt;

    private Sale(
            SaleId id,
            MedicineId medicineId,
            String medicineName,
            Quantity quantitySold,
            Money unitValue,
            Money totalValue,
            LocalDateTime saleDateTime) {
        validateData(medicineId, medicineName, quantitySold, unitValue, totalValue);

        this.id = id;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantitySold = quantitySold;
        this.unitValue = unitValue;
        this.totalValue = totalValue;
        this.saleDateTime = saleDateTime;
        this.createdAt = LocalDateTime.now();
    }

    // Factory method para crear una nueva venta
    public static Sale create(
            MedicineId medicineId,
            String medicineName,
            Integer quantitySold,
            Money unitValue) {
        Quantity quantity = Quantity.from(quantitySold);
        Money totalValue = unitValue.multiply(quantitySold);

        return new Sale(
                SaleId.create(),
                medicineId,
                medicineName,
                quantity,
                unitValue,
                totalValue,
                LocalDateTime.now()
        );
    }

    // Factory method para reconstruir desde persistencia
    public static Sale reconstruct(
            SaleId id,
            MedicineId medicineId,
            String medicineName,
            Integer quantitySold,
            Money unitValue,
            Money totalValue,
            LocalDateTime saleDateTime,
            LocalDateTime createdAt) {
        Sale sale = new Sale(
                id,
                medicineId,
                medicineName,
                Quantity.from(quantitySold),
                unitValue,
                totalValue,
                saleDateTime
        );
        // Note: We can't set createdAt directly, but it's set in constructor
        return sale;
    }

    private void validateData(
            MedicineId medicineId,
            String medicineName,
            Quantity quantitySold,
            Money unitValue,
            Money totalValue) {
        Objects.requireNonNull(medicineId, "El ID del medicamento no puede ser nulo");
        if (medicineName == null || medicineName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento no puede estar vacío");
        }
        Objects.requireNonNull(quantitySold, "La cantidad vendida no puede ser nula");
        Objects.requireNonNull(unitValue, "El valor unitario no puede ser nulo");
        Objects.requireNonNull(totalValue, "El valor total no puede ser nulo");

        if (!quantitySold.isAvailable(quantitySold.getValue())) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
    }

    // Getters
    public SaleId getId() {
        return id;
    }

    public MedicineId getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public Quantity getQuantitySold() {
        return quantitySold;
    }

    public Money getUnitValue() {
        return unitValue;
    }

    public Money getTotalValue() {
        return totalValue;
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", medicineId=" + medicineId +
                ", medicineName='" + medicineName + '\'' +
                ", quantitySold=" + quantitySold +
                ", totalValue=" + totalValue +
                ", saleDateTime=" + saleDateTime +
                '}';
    }
}
