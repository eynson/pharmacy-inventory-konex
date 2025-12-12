package com.eynson.pharmacy_inventory.domain.usecase.sale;

import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.model.SaleId;
import com.eynson.pharmacy_inventory.domain.port.in.GetSalesByDateRangeUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("GetSalesByDateRangeUseCase Tests")
class GetSalesByDateRangeUseCaseImplTest {

    @Mock
    private SaleRepositoryPort saleRepository;
    private GetSalesByDateRangeUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetSalesByDateRangeUseCaseImpl(saleRepository);
    }

    @Test
    @DisplayName("should retrieve sales within date range with pagination")
    void shouldRetrieveSalesWithinDateRange() {
        // GIVEN
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        
        var sale1 = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from("sale-001"),
                MedicineId.from("med-001"),
                "Aspirin",
                50,
                Money.from(BigDecimal.valueOf(5.50)),
                Money.from(BigDecimal.valueOf(275.00)),
                LocalDateTime.of(2024, 12, 10, 10, 30),
                LocalDateTime.of(2024, 12, 10, 10, 30)
        ));
        
        var sale2 = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from("sale-002"),
                MedicineId.from("med-001"),
                "Aspirin",
                30,
                Money.from(BigDecimal.valueOf(5.50)),
                Money.from(BigDecimal.valueOf(165.00)),
                LocalDateTime.of(2024, 12, 20, 14, 15),
                LocalDateTime.of(2024, 12, 20, 14, 15)
        ));

        var sales = List.of(sale1, sale2);

        when(saleRepository.findByDateRange(startDate, endDate, 1, 10))
                .thenReturn(sales);

        var query = new GetSalesByDateRangeUseCase.GetSalesByDateRangeQuery(
                startDate, endDate, 1, 10
        );

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(50, result.get(0).getQuantitySold().getValue());
        assertEquals(30, result.get(1).getQuantitySold().getValue());
        verify(saleRepository, times(1)).findByDateRange(startDate, endDate, 1, 10);
    }

    @Test
    @DisplayName("should apply date range filter on sales")
    void shouldApplyDateRangeFilter() {
        // GIVEN
        LocalDateTime startDate = LocalDateTime.of(2024, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 11, 30, 23, 59);
        
        var sale = Sale.reconstruct(new Sale.SaleReconstructionData(
                SaleId.from("sale-003"),
                MedicineId.from("med-002"),
                "Ibuprofen",
                75,
                Money.from(BigDecimal.valueOf(3.75)),
                Money.from(BigDecimal.valueOf(281.25)),
                LocalDateTime.of(2024, 11, 15, 9, 0),
                LocalDateTime.of(2024, 11, 15, 9, 0)
        ));

        var sales = List.of(sale);

        when(saleRepository.findByDateRange(startDate, endDate, 1, 10))
                .thenReturn(sales);

        var query = new GetSalesByDateRangeUseCase.GetSalesByDateRangeQuery(
                startDate, endDate, 1, 10
        );

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ibuprofen", result.get(0).getMedicineName());
        verify(saleRepository, times(1)).findByDateRange(startDate, endDate, 1, 10);
    }

    @Test
    @DisplayName("should return empty list when no sales in date range")
    void shouldReturnEmptyListWhenNoSalesInRange() {
        // GIVEN
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);

        when(saleRepository.findByDateRange(startDate, endDate, 1, 10))
                .thenReturn(List.of());

        var query = new GetSalesByDateRangeUseCase.GetSalesByDateRangeQuery(
                startDate, endDate, 1, 10
        );

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(saleRepository, times(1)).findByDateRange(startDate, endDate, 1, 10);
    }

    @Test
    @DisplayName("should handle different pagination parameters")
    void shouldHandleDifferentPaginationParameters() {
        // GIVEN
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        when(saleRepository.findByDateRange(startDate, endDate, 2, 20))
                .thenReturn(List.of());

        var query = new GetSalesByDateRangeUseCase.GetSalesByDateRangeQuery(
                startDate, endDate, 2, 20
        );

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(saleRepository, times(1)).findByDateRange(startDate, endDate, 2, 20);
    }

    @Test
    @DisplayName("should verify repository is called with correct parameters")
    void shouldVerifyRepositoryCallWithCorrectParameters() {
        // GIVEN
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        when(saleRepository.findByDateRange(any(LocalDateTime.class), any(LocalDateTime.class), anyInt(), anyInt()))
                .thenReturn(List.of());

        var query = new GetSalesByDateRangeUseCase.GetSalesByDateRangeQuery(
                startDate, endDate, 1, 10
        );

        // WHEN
        useCase.execute(query);

        // THEN
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(saleRepository).findByDateRange(startCaptor.capture(), endCaptor.capture(), pageCaptor.capture(), pageSizeCaptor.capture());
        
        assertEquals(startDate, startCaptor.getValue());
        assertEquals(endDate, endCaptor.getValue());
        assertEquals(1, pageCaptor.getValue());
        assertEquals(10, pageSizeCaptor.getValue());
    }
}
