package com.eynson.pharmacy_inventory.infrastructure.adapter;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.infrastructure.entity.MedicineEntity;
import com.eynson.pharmacy_inventory.infrastructure.repository.MedicineJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("MedicineRepositoryAdapter Tests")
class MedicineRepositoryAdapterTest {

    @Mock
    private MedicineJpaRepository medicineJpaRepository;
    private MedicineRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new MedicineRepositoryAdapter(medicineJpaRepository);
    }

    @Test
    @DisplayName("should save medicine and return domain object")
    void shouldSaveMedicineAndReturnDomainObject() {
        // GIVEN
        var medicine = Medicine.create(
                "Aspirin",
                "Bayer",
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now().plusYears(1),
                100,
                Money.from(BigDecimal.valueOf(5.50))
        );
        
        var entity = MedicineEntity.builder()
                .id(medicine.getId().getValue())
                .name(medicine.getName())
                .factoryLaboratory(medicine.getFactoryLaboratory())
                .manufacturingDate(medicine.getManufacturingDate())
                .expirationDate(medicine.getExpirationDate())
                .quantityInStock(medicine.getQuantityInStock().getValue())
                .unitValue(medicine.getUnitValue().getAmount())
                .createdAt(medicine.getCreatedAt())
                .updatedAt(medicine.getUpdatedAt())
                .build();

        when(medicineJpaRepository.save(any(MedicineEntity.class)))
                .thenReturn(entity);

        // WHEN
        var result = adapter.save(medicine);

        // THEN
        assertNotNull(result);
        assertEquals("Aspirin", result.getName());
        assertEquals("Bayer", result.getFactoryLaboratory());
        verify(medicineJpaRepository, times(1)).save(any(MedicineEntity.class));
    }

    @Test
    @DisplayName("should find medicine by ID")
    void shouldFindMedicineById() {
        // GIVEN
        String medicineId = "med-001";
        var entity = MedicineEntity.builder()
                .id(medicineId)
                .name("Aspirin")
                .factoryLaboratory("Bayer")
                .manufacturingDate(LocalDateTime.now().minusYears(1))
                .expirationDate(LocalDateTime.now().plusYears(1))
                .quantityInStock(100)
                .unitValue(BigDecimal.valueOf(5.50))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(medicineJpaRepository.findById(medicineId))
                .thenReturn(Optional.of(entity));

        // WHEN
        var result = adapter.findById(medicineId);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("Aspirin", result.get().getName());
        verify(medicineJpaRepository, times(1)).findById(medicineId);
    }

    @Test
    @DisplayName("should return empty when medicine not found")
    void shouldReturnEmptyWhenMedicineNotFound() {
        // GIVEN
        String medicineId = "non-existent";

        when(medicineJpaRepository.findById(medicineId))
                .thenReturn(Optional.empty());

        // WHEN
        var result = adapter.findById(medicineId);

        // THEN
        assertFalse(result.isPresent());
        verify(medicineJpaRepository, times(1)).findById(medicineId);
    }

    @Test
    @DisplayName("should delete medicine by ID")
    void shouldDeleteMedicineById() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);

        doNothing().when(medicineJpaRepository).deleteById(medicineId);

        // WHEN
        adapter.deleteById(id);

        // THEN
        verify(medicineJpaRepository, times(1)).deleteById(medicineId);
    }

    @Test
    @DisplayName("should check if medicine exists by ID")
    void shouldCheckIfMedicineExistsById() {
        // GIVEN
        String medicineId = "med-001";
        MedicineId id = MedicineId.from(medicineId);

        when(medicineJpaRepository.existsById(medicineId))
                .thenReturn(true);

        // WHEN
        var result = adapter.existsById(id);

        // THEN
        assertTrue(result);
        verify(medicineJpaRepository, times(1)).existsById(medicineId);
    }

    @Test
    @DisplayName("should find medicines with pagination")
    void shouldFindMedicinesWithPagination() {
        // GIVEN
        var entity = MedicineEntity.builder()
                .id("med-001")
                .name("Aspirin")
                .factoryLaboratory("Bayer")
                .manufacturingDate(LocalDateTime.now().minusYears(1))
                .expirationDate(LocalDateTime.now().plusYears(1))
                .quantityInStock(100)
                .unitValue(BigDecimal.valueOf(5.50))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<MedicineEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(medicineJpaRepository.findAll(pageable))
                .thenReturn(page);

        // WHEN
        var result = adapter.findAll(0, 10, "", "name");

        // THEN
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Aspirin", result.content().get(0).getName());
        verify(medicineJpaRepository, times(1)).findAll(pageable);
    }
}
