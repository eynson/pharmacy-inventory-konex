package com.eynson.pharmacy_inventory.infrastructure.adapter;

import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.model.SaleId;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.infrastructure.entity.SaleEntity;
import com.eynson.pharmacy_inventory.infrastructure.repository.SaleJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("SaleRepositoryAdapter Tests")
class SaleRepositoryAdapterTest {

    @Mock
    private SaleJpaRepository saleJpaRepository;
    private SaleRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new SaleRepositoryAdapter(saleJpaRepository);
    }

    @Test
    @DisplayName("should save sale and return domain object")
    void shouldSaveSaleAndReturnDomainObject() {
        // GIVEN
        var sale = Sale.reconstruct(
                SaleId.from("sale-001"),
                MedicineId.from("med-001"),
                "Aspirin",
                50,
                Money.from(BigDecimal.valueOf(5.50)),
                Money.from(BigDecimal.valueOf(275.00)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        
        var entity = new SaleEntity(
                sale.getId().getValue(),
                sale.getMedicineId().getValue(),
                sale.getMedicineName(),
                sale.getQuantitySold().getValue(),
                sale.getUnitValue().getAmount(),
                sale.getTotalValue().getAmount(),
                sale.getSaleDateTime(),
                sale.getCreatedAt()
        );

        when(saleJpaRepository.save(any(SaleEntity.class)))
                .thenReturn(entity);

        // WHEN
        var result = adapter.save(sale);

        // THEN
        assertNotNull(result);
        assertEquals("Aspirin", result.getMedicineName());
        assertEquals(50, result.getQuantitySold().getValue());
        verify(saleJpaRepository, times(1)).save(any(SaleEntity.class));
    }

    @Test
    @DisplayName("should find sale by ID string")
    void shouldFindSaleByIdString() {
        // GIVEN
        String saleId = "sale-001";
        var entity = new SaleEntity(
                saleId,
                "med-001",
                "Aspirin",
                50,
                BigDecimal.valueOf(5.50),
                BigDecimal.valueOf(275.00),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(saleJpaRepository.findById(saleId))
                .thenReturn(Optional.of(entity));

        // WHEN
        var result = adapter.findById(saleId);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("Aspirin", result.get().getMedicineName());
        verify(saleJpaRepository, times(1)).findById(saleId);
    }

    @Test
    @DisplayName("should return empty when sale not found")
    void shouldReturnEmptyWhenSaleNotFound() {
        // GIVEN
        String saleId = "non-existent";

        when(saleJpaRepository.findById(saleId))
                .thenReturn(Optional.empty());

        // WHEN
        var result = adapter.findById(saleId);

        // THEN
        assertFalse(result.isPresent());
        verify(saleJpaRepository, times(1)).findById(saleId);
    }

    @Test
    @DisplayName("should delete sale by ID")
    void shouldDeleteSaleById() {
        // GIVEN
        String saleId = "sale-001";
        SaleId id = SaleId.from(saleId);

        doNothing().when(saleJpaRepository).deleteById(saleId);

        // WHEN
        adapter.deleteById(id);

        // THEN
        verify(saleJpaRepository, times(1)).deleteById(saleId);
    }

    @Test
    @DisplayName("should count total sales")
    void shouldCountTotalSales() {
        // GIVEN
        when(saleJpaRepository.count())
                .thenReturn(5L);

        // WHEN
        var result = adapter.count();

        // THEN
        assertEquals(5L, result);
        verify(saleJpaRepository, times(1)).count();
    }

    @Test
    @DisplayName("should find sales by date range")
    void shouldFindSalesByDateRange() {
        // GIVEN
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        var entity1 = new SaleEntity(
                "sale-001",
                "med-001",
                "Aspirin",
                50,
                BigDecimal.valueOf(5.50),
                BigDecimal.valueOf(275.00),
                LocalDateTime.of(2024, 12, 10, 10, 30),
                LocalDateTime.of(2024, 12, 10, 10, 30)
        );

        when(saleJpaRepository.findByDateRange(startDate, endDate))
                .thenReturn(List.of(entity1));

        // WHEN
        var result = adapter.findByDateRange(startDate, endDate, 0, 10);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aspirin", result.get(0).getMedicineName());
        verify(saleJpaRepository, times(1)).findByDateRange(startDate, endDate);
    }
}
