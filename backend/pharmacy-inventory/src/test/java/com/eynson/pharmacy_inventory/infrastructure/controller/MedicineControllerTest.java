package com.eynson.pharmacy_inventory.infrastructure.controller;

import com.eynson.pharmacy_inventory.application.dto.request.CreateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.response.MedicineResponse;
import com.eynson.pharmacy_inventory.application.service.MedicineApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("MedicineController Tests")
class MedicineControllerTest {

    @Mock
    private MedicineApplicationService medicineApplicationService;
    private MedicineController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new MedicineController(medicineApplicationService);
    }

    @Test
    @DisplayName("should create medicine and return CREATED status")
    void shouldCreateMedicine() {
        var request = new CreateMedicineRequest(
                "Aspirin",
                "Bayer",
                "2024-01-01T10:00:00",
                "2026-01-01T10:00:00",
                100,
                new BigDecimal("5.50")
        );

        var response = new MedicineResponse(
                "med-id-1",
                "Aspirin",
                "Bayer",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(2),
                100,
                new BigDecimal("5.50"),
                false
        );

        when(medicineApplicationService.createMedicine(any())).thenReturn(response);

        var result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(medicineApplicationService, times(1)).createMedicine(any());
    }

    @Test
    @DisplayName("should get medicine by ID and return OK status")
    void shouldGetMedicineById() {
        var medicineId = "med-id-1";
        var response = new MedicineResponse(
                medicineId,
                "Aspirin",
                "Bayer",
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(2),
                100,
                new BigDecimal("5.50"),
                false
        );

        when(medicineApplicationService.getMedicineById(medicineId)).thenReturn(response);

        var result = controller.getById(medicineId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(medicineApplicationService, times(1)).getMedicineById(medicineId);
    }

    @Test
    @DisplayName("should delete medicine and return NO_CONTENT status")
    void shouldDeleteMedicine() {
        var medicineId = "med-id-1";
        doNothing().when(medicineApplicationService).deleteMedicine(medicineId);

        var result = controller.delete(medicineId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(medicineApplicationService, times(1)).deleteMedicine(medicineId);
    }
}
