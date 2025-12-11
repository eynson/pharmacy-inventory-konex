package com.eynson.pharmacy_inventory.infrastructure.controller;

import com.eynson.pharmacy_inventory.application.dto.request.CreateSaleRequest;
import com.eynson.pharmacy_inventory.application.dto.response.SaleResponse;
import com.eynson.pharmacy_inventory.application.service.SaleApplicationService;
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

@DisplayName("SaleController Tests")
class SaleControllerTest {

    @Mock
    private SaleApplicationService saleApplicationService;
    private SaleController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new SaleController(saleApplicationService);
    }

    @Test
    @DisplayName("should create sale and return CREATED status")
    void shouldCreateSale() {
        var request = new CreateSaleRequest("med-id-1", 50);

        var response = new SaleResponse(
                "sale-id-1",
                "med-id-1",
                "Aspirin",
                50,
                new BigDecimal("5.50"),
                new BigDecimal("275.00"),
                LocalDateTime.now()
        );

        when(saleApplicationService.createSale(any())).thenReturn(response);

        var result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(saleApplicationService, times(1)).createSale(any());
    }

    @Test
    @DisplayName("should get sales by date range and return OK status")
    void shouldGetSalesByDateRange() {
        var startDate = LocalDateTime.now().minusDays(7).toString();
        var endDate = LocalDateTime.now().toString();

        when(saleApplicationService.getSalesByDateRange(any()))
                .thenReturn(new com.eynson.pharmacy_inventory.application.dto.response.PagedSaleResponse(
                        java.util.List.of(),
                        0,
                        10,
                        0L,
                        0
                ));

        var result = controller.getSalesByDateRange(startDate, endDate, 0, 10);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(saleApplicationService, times(1)).getSalesByDateRange(any());
    }
}
