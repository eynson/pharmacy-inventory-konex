package com.eynson.pharmacy_inventory.infrastructure.controller;

import com.eynson.pharmacy_inventory.application.dto.request.CreateSaleRequest;
import com.eynson.pharmacy_inventory.application.dto.request.GetSalesByDateRangeRequest;
import com.eynson.pharmacy_inventory.application.dto.response.PagedSaleResponse;
import com.eynson.pharmacy_inventory.application.dto.response.SaleResponse;
import com.eynson.pharmacy_inventory.application.service.SaleApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {
    private final SaleApplicationService saleApplicationService;

    public SaleController(SaleApplicationService saleApplicationService) {
        this.saleApplicationService = saleApplicationService;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody CreateSaleRequest request) {
        var response = saleApplicationService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PagedSaleResponse> getSalesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        var request = new GetSalesByDateRangeRequest(startDate, endDate, page, pageSize);
        var response = saleApplicationService.getSalesByDateRange(request);
        return ResponseEntity.ok(response);
    }
}
