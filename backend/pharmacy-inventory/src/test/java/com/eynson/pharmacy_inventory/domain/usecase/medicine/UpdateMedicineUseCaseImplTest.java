package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.port.in.UpdateMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("UpdateMedicineUseCase Tests")
class UpdateMedicineUseCaseImplTest {

    @Mock
    private MedicineRepositoryPort medicineRepository;
    private UpdateMedicineUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UpdateMedicineUseCaseImpl(medicineRepository);
    }

    @Test
    @DisplayName("should update medicine successfully with valid data")
    void shouldUpdateMedicineSuccessfully() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);
        var existingMedicine = Medicine.create(
                "Old Name",
                "Old Lab",
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now().plusYears(1),
                50,
                Money.from(BigDecimal.valueOf(3.00))
        );

        var command = new UpdateMedicineUseCase.UpdateMedicineCommand(
                medicineId,
                "Updated Aspirin",
                "Bayer",
                "2024-01-01",
                "2026-01-01",
                100,
                "5.50"
        );

        when(medicineRepository.findById(id))
                .thenReturn(Optional.of(existingMedicine));
        when(medicineRepository.save(any(Medicine.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        var result = useCase.execute(command);

        // THEN
        assertNotNull(result);
        assertEquals("Updated Aspirin", result.getName());
        assertEquals("Bayer", result.getFactoryLaboratory());
        assertEquals(100, result.getQuantityInStock().getValue());
        verify(medicineRepository, times(1)).findById(id);
        verify(medicineRepository, times(1)).save(any(Medicine.class));
    }

    @Test
    @DisplayName("should throw exception when medicine not found")
    void shouldThrowExceptionWhenMedicineNotFound() {
        // GIVEN
        String medicineId = "non-existent-id";
        MedicineId id = MedicineId.from(medicineId);
        var command = new UpdateMedicineUseCase.UpdateMedicineCommand(
                medicineId,
                "Aspirin",
                "Bayer",
                "2024-01-01",
                "2026-01-01",
                100,
                "5.50"
        );

        when(medicineRepository.findById(id))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(MedicineNotFoundException.class, () -> useCase.execute(command));
        verify(medicineRepository, times(1)).findById(id);
        verify(medicineRepository, never()).save(any());
    }

    @Test
    @DisplayName("should keep existing name when null is provided")
    void shouldKeepExistingNameWhenNullProvided() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);
        var existingMedicine = Medicine.create(
                "Original Name",
                "Old Lab",
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now().plusYears(1),
                50,
                Money.from(BigDecimal.valueOf(3.00))
        );

        var command = new UpdateMedicineUseCase.UpdateMedicineCommand(
                medicineId,
                null,  // Name is null - should keep existing
                "Bayer",
                "2024-01-01",
                "2026-01-01",
                100,
                "5.50"
        );

        when(medicineRepository.findById(id))
                .thenReturn(Optional.of(existingMedicine));
        when(medicineRepository.save(any(Medicine.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        var result = useCase.execute(command);

        // THEN - Should keep the original name when null is provided
        assertNotNull(result);
        assertEquals("Original Name", result.getName());
        verify(medicineRepository, times(1)).findById(id);
        verify(medicineRepository, times(1)).save(any(Medicine.class));
    }

    @Test
    @DisplayName("should throw exception when stock is negative")
    void shouldThrowExceptionWhenStockIsNegative() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);
        var existingMedicine = Medicine.create(
                "Old Name",
                "Old Lab",
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now().plusYears(1),
                50,
                Money.from(BigDecimal.valueOf(3.00))
        );

        var command = new UpdateMedicineUseCase.UpdateMedicineCommand(
                medicineId,
                "Aspirin",
                "Bayer",
                "2024-01-01",
                "2026-01-01",
                -10,
                "5.50"
        );

        when(medicineRepository.findById(id))
                .thenReturn(Optional.of(existingMedicine));

        // WHEN & THEN
        assertThrows(Exception.class, () -> useCase.execute(command));
        verify(medicineRepository, never()).save(any());
    }
}
