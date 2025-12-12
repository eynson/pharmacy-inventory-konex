package com.eynson.pharmacy_inventory.domain.usecase.sale;

import com.eynson.pharmacy_inventory.domain.exception.SaleNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.model.SaleId;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GetSaleByIdUseCase Tests")
class GetSaleByIdUseCaseImplTest {

    @Mock
    private SaleRepositoryPort saleRepository;
    private GetSaleByIdUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetSaleByIdUseCaseImpl(saleRepository);
    }

    @Test
    @DisplayName("should retrieve sale successfully by ID")
    void shouldRetrieveSaleSuccessfully() {
        // GIVEN
        String saleIdStr = "sale-001";
        
        var sale = Sale.reconstruct(
                SaleId.from(saleIdStr),
                MedicineId.from("med-001"),
                "Aspirin",
                50,
                Money.from(BigDecimal.valueOf(5.50)),
                Money.from(BigDecimal.valueOf(275.00)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(saleRepository.findById(saleIdStr))
                .thenReturn(Optional.of(sale));

        // WHEN
        var result = useCase.execute(saleIdStr);

        // THEN
        assertNotNull(result);
        assertEquals(50, result.getQuantitySold().getValue());
        assertEquals("Aspirin", result.getMedicineName());
        verify(saleRepository, times(1)).findById(saleIdStr);
    }

    @Test
    @DisplayName("should throw exception when sale not found")
    void shouldThrowExceptionWhenSaleNotFound() {
        // GIVEN
        String saleIdStr = "non-existent-id";
        
        when(saleRepository.findById(saleIdStr))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(SaleNotFoundException.class, () -> useCase.execute(saleIdStr));
        verify(saleRepository, times(1)).findById(saleIdStr);
    }

    @Test
    @DisplayName("should return sale with correct details")
    void shouldReturnSaleWithCorrectDetails() {
        // GIVEN
        String saleIdStr = "sale-002";
        
        var sale = Sale.reconstruct(
                SaleId.from(saleIdStr),
                MedicineId.from("med-002"),
                "Ibuprofen",
                100,
                Money.from(BigDecimal.valueOf(3.75)),
                Money.from(BigDecimal.valueOf(375.00)),
                LocalDateTime.of(2024, 12, 1, 10, 30),
                LocalDateTime.of(2024, 12, 1, 10, 30)
        );

        when(saleRepository.findById(saleIdStr))
                .thenReturn(Optional.of(sale));

        // WHEN
        var result = useCase.execute(saleIdStr);

        // THEN
        assertNotNull(result);
        assertEquals("Ibuprofen", result.getMedicineName());
        assertEquals(100, result.getQuantitySold().getValue());
        assertEquals(BigDecimal.valueOf(3.75), result.getUnitValue().getAmount());
    }

    @Test
    @DisplayName("should call repository with correct sale ID")
    void shouldCallRepositoryWithCorrectSaleId() {
        // GIVEN
        String saleIdStr = "sale-003";
        
        var sale = Sale.reconstruct(
                SaleId.from(saleIdStr),
                MedicineId.from("med-003"),
                "Paracetamol",
                75,
                Money.from(BigDecimal.valueOf(2.25)),
                Money.from(BigDecimal.valueOf(168.75)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(saleRepository.findById(saleIdStr))
                .thenReturn(Optional.of(sale));

        // WHEN
        useCase.execute(saleIdStr);

        // THEN
        verify(saleRepository, times(1)).findById(saleIdStr);
    }
}
