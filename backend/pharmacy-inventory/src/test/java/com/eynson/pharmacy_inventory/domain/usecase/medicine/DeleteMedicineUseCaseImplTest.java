package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.exception.MedicineNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("DeleteMedicineUseCase Tests")
class DeleteMedicineUseCaseImplTest {

    @Mock
    private MedicineRepositoryPort medicineRepository;
    private DeleteMedicineUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new DeleteMedicineUseCaseImpl(medicineRepository);
    }

    @Test
    @DisplayName("should delete medicine successfully")
    void shouldDeleteMedicineSuccessfully() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);
        
        when(medicineRepository.existsById(id))
                .thenReturn(true);
        doNothing().when(medicineRepository).deleteById(id);

        // WHEN
        assertDoesNotThrow(() -> useCase.execute(medicineId));

        // THEN
        verify(medicineRepository, times(1)).existsById(id);
        verify(medicineRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("should throw exception when medicine not found")
    void shouldThrowExceptionWhenMedicineNotFound() {
        // GIVEN
        String medicineId = "non-existent-id";
        MedicineId id = MedicineId.from(medicineId);
        when(medicineRepository.existsById(id))
                .thenReturn(false);

        // WHEN & THEN
        assertThrows(MedicineNotFoundException.class, () -> useCase.execute(medicineId));
        verify(medicineRepository, times(1)).existsById(id);
        verify(medicineRepository, never()).deleteById(any(MedicineId.class));
    }

    @Test
    @DisplayName("should verify deletion is called after checking existence")
    void shouldVerifyCallOrder() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);
        
        when(medicineRepository.existsById(id))
                .thenReturn(true);
        doNothing().when(medicineRepository).deleteById(id);

        // WHEN
        useCase.execute(medicineId);

        // THEN - Verify call order: existsById before deleteById
        InOrder inOrder = inOrder(medicineRepository);
        inOrder.verify(medicineRepository).existsById(id);
        inOrder.verify(medicineRepository).deleteById(id);
    }

    @Test
    @DisplayName("should not call delete if medicine not found")
    void shouldNotCallDeleteIfNotFound() {
        // GIVEN
        String medicineId = "non-existent-id";
        MedicineId id = MedicineId.from(medicineId);
        when(medicineRepository.existsById(id))
                .thenReturn(false);

        // WHEN & THEN
        assertThrows(MedicineNotFoundException.class, () -> useCase.execute(medicineId));
        
        // Verify delete was never called
        verify(medicineRepository, never()).deleteById(any(MedicineId.class));
    }
}
