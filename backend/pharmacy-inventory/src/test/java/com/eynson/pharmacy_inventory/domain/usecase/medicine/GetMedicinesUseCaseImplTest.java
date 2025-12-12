package com.eynson.pharmacy_inventory.domain.usecase.medicine;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.port.in.GetMedicinesUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
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

@DisplayName("GetMedicinesUseCase Tests")
class GetMedicinesUseCaseImplTest {

    @Mock
    private MedicineRepositoryPort medicineRepository;
    private GetMedicinesUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetMedicinesUseCaseImpl(medicineRepository);
    }

    @Test
    @DisplayName("should retrieve medicines with pagination")
    void shouldRetrieveMedicinesWithPagination() {
        // GIVEN
        var medicine1 = Medicine.create(
                "Aspirin",
                "Bayer",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                100,
                Money.from(BigDecimal.valueOf(5.50))
        );
        var medicine2 = Medicine.create(
                "Ibuprofen",
                "Pfizer",
                LocalDateTime.of(2023, 6, 15, 0, 0),
                LocalDateTime.of(2025, 6, 15, 0, 0),
                250,
                Money.from(BigDecimal.valueOf(3.75))
        );

        var medicines = List.of(medicine1, medicine2);
        var pagedResult = new com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort.PaginatedResult<Medicine>(
                medicines, 1, 2L, 1, 10
        );

        when(medicineRepository.findAll(1, 10, "", "name"))
                .thenReturn(pagedResult);

        var query = new GetMedicinesUseCase.GetMedicinesQuery(1, 10, "", "name");

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals(1, result.totalPages());
        assertEquals(2L, result.totalElements());
        assertEquals(1, result.currentPage());
        assertEquals(10, result.pageSize());
        verify(medicineRepository, times(1)).findAll(1, 10, "", "name");
    }

    @Test
    @DisplayName("should apply search filter on medicines")
    void shouldApplySearchFilter() {
        // GIVEN
        var medicine = Medicine.create(
                "Aspirin",
                "Bayer",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                100,
                Money.from(BigDecimal.valueOf(5.50))
        );

        var medicines = List.of(medicine);
        var pagedResult = new com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort.PaginatedResult<Medicine>(
                medicines, 1, 1L, 1, 10
        );

        when(medicineRepository.findAll(1, 10, "Aspirin", "name"))
                .thenReturn(pagedResult);

        var query = new GetMedicinesUseCase.GetMedicinesQuery(1, 10, "Aspirin", "name");

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Aspirin", result.content().get(0).getName());
        verify(medicineRepository, times(1)).findAll(1, 10, "Aspirin", "name");
    }

    @Test
    @DisplayName("should return empty list when no medicines found")
    void shouldReturnEmptyListWhenNoMedicinesFound() {
        // GIVEN
        var medicines = List.<Medicine>of();
        var pagedResult = new com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort.PaginatedResult<Medicine>(
                medicines, 0, 0L, 1, 10
        );

        when(medicineRepository.findAll(1, 10, "NonExistent", "name"))
                .thenReturn(pagedResult);

        var query = new GetMedicinesUseCase.GetMedicinesQuery(1, 10, "NonExistent", "name");

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertTrue(result.content().isEmpty());
        assertEquals(0, result.totalPages());
        assertEquals(0L, result.totalElements());
    }

    @Test
    @DisplayName("should handle different pagination parameters")
    void shouldHandleDifferentPaginationParameters() {
        // GIVEN
        var medicines = List.<Medicine>of();
        var pagedResult = new com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort.PaginatedResult<Medicine>(
                medicines, 0, 0L, 2, 20
        );

        when(medicineRepository.findAll(2, 20, "", "factory"))
                .thenReturn(pagedResult);

        var query = new GetMedicinesUseCase.GetMedicinesQuery(2, 20, "", "factory");

        // WHEN
        var result = useCase.execute(query);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.currentPage());
        assertEquals(20, result.pageSize());
        verify(medicineRepository, times(1)).findAll(2, 20, "", "factory");
    }

    @Test
    @DisplayName("should verify repository is called with correct parameters")
    void shouldVerifyRepositoryCallWithCorrectParameters() {
        // GIVEN
        var pagedResult = new com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort.PaginatedResult<Medicine>(
                List.of(), 0, 0L, 1, 10
        );

        when(medicineRepository.findAll(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(pagedResult);

        var query = new GetMedicinesUseCase.GetMedicinesQuery(1, 10, "test", "name");

        // WHEN
        useCase.execute(query);

        // THEN
        ArgumentCaptor<Integer> pageCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> searchCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> sortCaptor = ArgumentCaptor.forClass(String.class);

        verify(medicineRepository).findAll(pageCaptor.capture(), pageSizeCaptor.capture(), searchCaptor.capture(), sortCaptor.capture());
        
        assertEquals(1, pageCaptor.getValue());
        assertEquals(10, pageSizeCaptor.getValue());
        assertEquals("test", searchCaptor.getValue());
        assertEquals("name", sortCaptor.getValue());
    }
}
