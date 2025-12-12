package com.eynson.pharmacy_inventory.application.mapper;

import com.eynson.pharmacy_inventory.application.dto.response.SaleResponse;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.model.SaleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SaleMapper Tests")
class SaleMapperTest {

    private SaleMapper saleMapper;

    @BeforeEach
    void setUp() {
        saleMapper = new SaleMapper();
    }

    @Test
    @DisplayName("should map sale to response")
    void shouldMapSaleToResponse() {
        // GIVEN
        var sale = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from("sale-001"),
                MedicineId.from("med-001"),
                "Aspirin",
                50,
                Money.from(BigDecimal.valueOf(5.50)),
                Money.from(BigDecimal.valueOf(275.00)),
                LocalDateTime.of(2024, 12, 10, 10, 30),
                LocalDateTime.of(2024, 12, 10, 10, 30)
        ));

        // WHEN
        var response = saleMapper.toResponse(sale);

        // THEN
        assertNotNull(response);
        assertEquals("sale-001", response.id());
        assertEquals("med-001", response.medicineId());
        assertEquals("Aspirin", response.medicineName());
        assertEquals(50, response.quantitySold());
        assertEquals(0, new java.math.BigDecimal("5.50").compareTo(response.unitValue()));
        assertEquals(0, new java.math.BigDecimal("275.00").compareTo(response.totalValue()));
    }

    @Test
    @DisplayName("should preserve sale details in response mapping")
    void shouldPreserveSaleDetailsInResponseMapping() {
        // GIVEN
        var saleDateTime = LocalDateTime.of(2024, 11, 15, 14, 45);
        var sale = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from("sale-002"),
                MedicineId.from("med-002"),
                "Ibuprofen",
                100,
                Money.from(BigDecimal.valueOf(3.75)),
                Money.from(BigDecimal.valueOf(375.00)),
                saleDateTime,
                LocalDateTime.now()
        ));

        // WHEN
        var response = saleMapper.toResponse(sale);

        // THEN
        assertEquals("sale-002", response.id());
        assertEquals("med-002", response.medicineId());
        assertEquals("Ibuprofen", response.medicineName());
        assertEquals(100, response.quantitySold());
        assertEquals(0, new java.math.BigDecimal("3.75").compareTo(response.unitValue()));
        assertEquals(0, new java.math.BigDecimal("375.00").compareTo(response.totalValue()));
        assertEquals(saleDateTime, response.saleDate());
    }

    @Test
    @DisplayName("should handle large values in sale response mapping")
    void shouldHandleLargeValuesInSaleResponseMapping() {
        // GIVEN
        var sale = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from("sale-003"),
                MedicineId.from("med-003"),
                "Premium Medicine",
                1000,
                Money.from(BigDecimal.valueOf(99.99)),
                Money.from(BigDecimal.valueOf(99990.00)),
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        // WHEN
        var response = saleMapper.toResponse(sale);

        // THEN
        assertEquals(1000, response.quantitySold());
        assertEquals(0, new java.math.BigDecimal("99.99").compareTo(response.unitValue()));
        assertEquals(0, new java.math.BigDecimal("99990.00").compareTo(response.totalValue()));
    }

    @Test
    @DisplayName("should map sale with decimal values correctly")
    void shouldMapSaleWithDecimalValuesCorrectly() {
        // GIVEN
        var sale = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from("sale-004"),
                MedicineId.from("med-004"),
                "Paracetamol",
                75,
                Money.from(BigDecimal.valueOf(2.25)),
                Money.from(BigDecimal.valueOf(168.75)),
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        // WHEN
        var response = saleMapper.toResponse(sale);

        // THEN
        assertEquals(BigDecimal.valueOf(2.25), response.unitValue());
        assertEquals(BigDecimal.valueOf(168.75), response.totalValue());
    }

    @Test
    @DisplayName("should map sale with all fields populated")
    void shouldMapSaleWithAllFieldsPopulated() {
        // GIVEN
        var saleId = "sale-complete";
        var medicineId = "med-complete";
        var medicineName = "Complete Medicine";
        var quantitySold = 77;
        var unitValue = BigDecimal.valueOf(7.77);
        var totalValue = BigDecimal.valueOf(599.29);
        var saleDateTime = LocalDateTime.of(2024, 10, 20, 15, 00);

        var sale = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from(saleId),
                MedicineId.from(medicineId),
                medicineName,
                quantitySold,
                Money.from(unitValue),
                Money.from(totalValue),
                saleDateTime,
                LocalDateTime.now()
        ));

        // WHEN
        var response = saleMapper.toResponse(sale);

        // THEN
        assertEquals(saleId, response.id());
        assertEquals(medicineId, response.medicineId());
        assertEquals(medicineName, response.medicineName());
        assertEquals(quantitySold, response.quantitySold());
        assertEquals(unitValue, response.unitValue());
        assertEquals(totalValue, response.totalValue());
        assertEquals(saleDateTime, response.saleDate());
    }
}
