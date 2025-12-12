package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Money;
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
import static org.mockito.Mockito.*;

@DisplayName("GetMedicineByIdUseCase Tests")
class GetMedicineByIdUseCaseImplTest {

    @Mock
    private MedicineRepositoryPort medicineRepository;
    private GetMedicineByIdUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetMedicineByIdUseCaseImpl(medicineRepository);
    }

    @Test
    @DisplayName("should retrieve medicine successfully by ID")
    void shouldRetrieveMedicineSuccessfully() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);
        var medicine = Medicine.create(
                "Aspirin",
                "Bayer",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                100,
                Money.from(BigDecimal.valueOf(5.50))
        );

        when(medicineRepository.findById(id))
                .thenReturn(Optional.of(medicine));

        // WHEN
        var result = useCase.execute(medicineId);

        // THEN
        assertNotNull(result);
        assertEquals("Aspirin", result.getName());
        assertEquals("Bayer", result.getFactoryLaboratory());
        assertEquals(100, result.getQuantityInStock().getValue());
        verify(medicineRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("should throw exception when medicine not found")
    void shouldThrowExceptionWhenMedicineNotFound() {
        // GIVEN
        String medicineId = "non-existent-id";
        MedicineId id = MedicineId.from(medicineId);
        when(medicineRepository.findById(id))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(MedicineNotFoundException.class, () -> useCase.execute(medicineId));
        verify(medicineRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("should return medicine with correct properties")
    void shouldReturnMedicineWithCorrectProperties() {
        // GIVEN
        String medicineId = "med-002";
        MedicineId id = MedicineId.from(medicineId);
        var expectedMedicine = Medicine.create(
                "Ibuprofen",
                "Pfizer",
                LocalDateTime.of(2023, 6, 15, 0, 0),
                LocalDateTime.of(2025, 6, 15, 0, 0),
                250,
                Money.from(BigDecimal.valueOf(3.75))
        );

        when(medicineRepository.findById(id))
                .thenReturn(Optional.of(expectedMedicine));

        // WHEN
        var result = useCase.execute(medicineId);

        // THEN
        assertNotNull(result);
        assertEquals("Ibuprofen", result.getName());
        assertEquals("Pfizer", result.getFactoryLaboratory());
        assertEquals(250, result.getQuantityInStock().getValue());
        assertNotNull(result.getUnitValue());
    }

    @Test
    @DisplayName("should call repository with correct medicine ID")
    void shouldCallRepositoryWithCorrectMedicineId() {
        // GIVEN
        String medicineId = "med-003";
        MedicineId id = MedicineId.from(medicineId);
        var medicine = Medicine.create(
                "Paracetamol",
                "GSK",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                500,
                Money.from(BigDecimal.valueOf(2.25))
        );

        when(medicineRepository.findById(id))
                .thenReturn(Optional.of(medicine));

        // WHEN
        useCase.execute(medicineId);

        // THEN
        verify(medicineRepository, times(1)).findById(id);
    }
}
