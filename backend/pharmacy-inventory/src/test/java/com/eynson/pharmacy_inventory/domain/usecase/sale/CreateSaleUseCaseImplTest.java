package com.eynson.pharmacy_inventory.domain.usecase.sale;

import com.eynson.pharmacy_inventory.domain.exception.InsufficientStockException;
import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.model.Quantity;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateSaleUseCase Tests")
class CreateSaleUseCaseImplTest {

    @Mock
    private MedicineRepositoryPort medicineRepository;
    @Mock
    private SaleRepositoryPort saleRepository;
    private CreateSaleUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateSaleUseCaseImpl(medicineRepository, saleRepository);
    }

    @Test
    @DisplayName("should create sale successfully when medicine exists and has sufficient stock")
    void shouldCreateSaleSuccessfully() {
        var medicine = Medicine.create(
                "Aspirin",
                "Bayer",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(2),
                100,
                Money.from(new BigDecimal("5.50"))
        );

        var command = new com.eynson.pharmacy_inventory.domain.port.in.CreateSaleUseCase.CreateSaleCommand(
                medicine.getId().getValue(),
                50
        );

        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);
        when(saleRepository.save(any(Sale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(50, result.getQuantitySold().getValue());
        verify(medicineRepository, times(1)).findById(medicine.getId());
        verify(medicineRepository, times(1)).save(any(Medicine.class));
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    @DisplayName("should throw exception when medicine not found")
    void shouldThrowExceptionWhenMedicineNotFound() {
        var command = new com.eynson.pharmacy_inventory.domain.port.in.CreateSaleUseCase.CreateSaleCommand(
                "non-existent-id",
                50
        );

        when(medicineRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        assertThrows(MedicineNotFoundException.class, () -> useCase.execute(command));
        verify(saleRepository, never()).save(any());
    }

    @Test
    @DisplayName("should throw exception when insufficient stock")
    void shouldThrowExceptionWhenInsufficientStock() {
        var medicine = Medicine.create(
                "Aspirin",
                "Bayer",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(2),
                30,
                Money.from(new BigDecimal("5.50"))
        );

        var command = new com.eynson.pharmacy_inventory.domain.port.in.CreateSaleUseCase.CreateSaleCommand(
                medicine.getId().getValue(),
                50
        );

        when(medicineRepository.findById(medicine.getId())).thenReturn(Optional.of(medicine));

        assertThrows(InsufficientStockException.class, () -> useCase.execute(command));
        verify(saleRepository, never()).save(any());
    }
}
