package com.eynson.pharmacy_inventory.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicines")
public class MedicineEntity {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "factory_laboratory", nullable = false, length = 255)
    private String factoryLaboratory;

    @Column(name = "manufacturing_date", nullable = false)
    private LocalDateTime manufacturingDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock;

    @Column(name = "unit_value", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal unitValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MedicineEntity() {
    }

    private MedicineEntity(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.factoryLaboratory = builder.factoryLaboratory;
        this.manufacturingDate = builder.manufacturingDate;
        this.expirationDate = builder.expirationDate;
        this.quantityInStock = builder.quantityInStock;
        this.unitValue = builder.unitValue;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String factoryLaboratory;
        private LocalDateTime manufacturingDate;
        private LocalDateTime expirationDate;
        private Integer quantityInStock;
        private java.math.BigDecimal unitValue;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder factoryLaboratory(String factoryLaboratory) {
            this.factoryLaboratory = factoryLaboratory;
            return this;
        }

        public Builder manufacturingDate(LocalDateTime manufacturingDate) {
            this.manufacturingDate = manufacturingDate;
            return this;
        }

        public Builder expirationDate(LocalDateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder quantityInStock(Integer quantityInStock) {
            this.quantityInStock = quantityInStock;
            return this;
        }

        public Builder unitValue(java.math.BigDecimal unitValue) {
            this.unitValue = unitValue;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public MedicineEntity build() {
            return new MedicineEntity(this);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactoryLaboratory() {
        return factoryLaboratory;
    }

    public void setFactoryLaboratory(String factoryLaboratory) {
        this.factoryLaboratory = factoryLaboratory;
    }

    public LocalDateTime getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDateTime manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public java.math.BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(java.math.BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
