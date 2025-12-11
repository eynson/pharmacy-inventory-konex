package com.eynson.pharmacy_inventory.infrastructure.controller;

import com.eynson.pharmacy_inventory.application.dto.request.CreateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.request.UpdateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.response.MedicineResponse;
import com.eynson.pharmacy_inventory.application.dto.response.PagedMedicineResponse;
import com.eynson.pharmacy_inventory.application.service.MedicineApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicines")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MedicineController {
    private final MedicineApplicationService medicineApplicationService;

    public MedicineController(MedicineApplicationService medicineApplicationService) {
        this.medicineApplicationService = medicineApplicationService;
    }

    @PostMapping
    public ResponseEntity<MedicineResponse> create(@RequestBody CreateMedicineRequest request) {
        var response = medicineApplicationService.createMedicine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> update(
            @PathVariable String id,
            @RequestBody UpdateMedicineRequest request) {
        var requestWithId = new UpdateMedicineRequest(
                id,
                request.name(),
                request.factoryLaboratory(),
                request.manufacturingDate(),
                request.expirationDate(),
                request.quantityInStock(),
                request.unitValue()
        );
        var response = medicineApplicationService.updateMedicine(requestWithId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        medicineApplicationService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getById(@PathVariable String id) {
        var response = medicineApplicationService.getMedicineById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PagedMedicineResponse> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String search) {
        var request = new com.eynson.pharmacy_inventory.application.dto.request.GetMedicinesRequest(page, pageSize, search);
        var response = medicineApplicationService.getMedicines(request);
        return ResponseEntity.ok(response);
    }
}
