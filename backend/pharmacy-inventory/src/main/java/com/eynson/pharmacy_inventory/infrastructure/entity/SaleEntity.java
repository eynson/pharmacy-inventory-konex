package com.eynson.pharmacy_inventory.infrastructure.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
public class SaleEntity {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "medicine_id", nullable = false, length = 36)
    private String medicineId;

    @Column(name = "medicine_name", nullable = false, length = 255)
    private String medicineName;

    @Column(name = "quantity_sold", nullable = false)
    private Integer quantitySold;

    @Column(name = "unit_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitValue;

    @Column(name = "total_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "sale_date_time", nullable = false)
    private LocalDateTime saleDateTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public SaleEntity() {
    }

    private SaleEntity(Builder builder) {
        this.id = builder.id;
        this.medicineId = builder.medicineId;
        this.medicineName = builder.medicineName;
        this.quantitySold = builder.quantitySold;
        this.unitValue = builder.unitValue;
        this.totalValue = builder.totalValue;
        this.saleDateTime = builder.saleDateTime;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String medicineId;
        private String medicineName;
        private Integer quantitySold;
        private BigDecimal unitValue;
        private BigDecimal totalValue;
        private LocalDateTime saleDateTime;
        private LocalDateTime createdAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder medicineId(String medicineId) {
            this.medicineId = medicineId;
            return this;
        }

        public Builder medicineName(String medicineName) {
            this.medicineName = medicineName;
            return this;
        }

        public Builder quantitySold(Integer quantitySold) {
            this.quantitySold = quantitySold;
            return this;
        }

        public Builder unitValue(BigDecimal unitValue) {
            this.unitValue = unitValue;
            return this;
        }

        public Builder totalValue(BigDecimal totalValue) {
            this.totalValue = totalValue;
            return this;
        }

        public Builder saleDateTime(LocalDateTime saleDateTime) {
            this.saleDateTime = saleDateTime;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SaleEntity build() {
            return new SaleEntity(this);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public void setSaleDateTime(LocalDateTime saleDateTime) {
        this.saleDateTime = saleDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
