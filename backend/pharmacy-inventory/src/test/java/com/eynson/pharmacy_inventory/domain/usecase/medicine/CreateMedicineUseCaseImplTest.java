package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.exception.InvalidMedicineDataException;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateMedicineUseCase Tests")
class CreateMedicineUseCaseImplTest {

    @Mock
    private MedicineRepositoryPort medicineRepository;
    private CreateMedicineUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateMedicineUseCaseImpl(medicineRepository);
    }

    @Test
    @DisplayName("should create medicine successfully with valid data")
    void shouldCreateMedicineSuccessfully() {
        var command = new com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase.CreateMedicineCommand(
                "Aspirin",
                "Bayer",
                "2024-01-01",
                "2026-01-01",
                100,
                "5.50"
        );

        when(medicineRepository.save(any(Medicine.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = useCase.execute(command);

        assertNotNull(result);
        assertEquals("Aspirin", result.getName());
        assertEquals("Bayer", result.getFactoryLaboratory());
        verify(medicineRepository, times(1)).save(any(Medicine.class));
    }

    @Test
    @DisplayName("should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        var command = new com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase.CreateMedicineCommand(
                null,
                "Bayer",
                "2024-01-01",
                "2026-01-01",
                100,
                "5.50"
        );

        assertThrows(InvalidMedicineDataException.class, () -> useCase.execute(command));
        verify(medicineRepository, never()).save(any());
    }

    @Test
    @DisplayName("should throw exception when expiration date is before manufacturing date")
    void shouldThrowExceptionWhenExpirationBeforeManufacturing() {
        var command = new com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase.CreateMedicineCommand(
                "Aspirin",
                "Bayer",
                "2026-01-01",
                "2024-01-01",
                100,
                "5.50"
        );

        assertThrows(InvalidMedicineDataException.class, () -> useCase.execute(command));
        verify(medicineRepository, never()).save(any());
    }
}
