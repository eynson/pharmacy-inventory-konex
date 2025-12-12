package com.eynson.pharmacy_inventory.application.mapper;

import com.eynson.pharmacy_inventory.application.dto.request.CreateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.request.UpdateMedicineRequest;
import com.eynson.pharmacy_inventory.application.dto.response.MedicineResponse;
import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.port.in.CreateMedicineUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.UpdateMedicineUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("MedicineMapper Tests")
class MedicineMapperTest {

    @Autowired
    private MedicineMapper medicineMapper;

    @Test
    @DisplayName("should map medicine to response")
    void shouldMapMedicineToResponse() {
        // GIVEN
        var medicine = Medicine.create(
                "Aspirin",
                "Bayer",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                100,
                Money.from(BigDecimal.valueOf(5.50))
        );

        // WHEN
        var response = medicineMapper.toResponse(medicine);

        // THEN
        assertNotNull(response);
        assertEquals("Aspirin", response.name());
        assertEquals("Bayer", response.factoryLaboratory());
        assertEquals(100, response.quantityInStock());
        assertEquals(0, BigDecimal.valueOf(5.50).compareTo(response.unitValue()));
        assertFalse(response.isExpired());
    }

    @Test
    @DisplayName("should map create medicine request to command")
    void shouldMapCreateMedicineRequestToCommand() {
        // GIVEN
        var request = new CreateMedicineRequest(
                "Ibuprofen",
                "Pfizer",
                "2023-06-15",
                "2025-06-15",
                250,
                BigDecimal.valueOf(3.75)
        );

        // WHEN
        var command = medicineMapper.toCreateCommand(request);

        // THEN
        assertNotNull(command);
        assertEquals("Ibuprofen", command.name());
        assertEquals("Pfizer", command.factoryLaboratory());
        assertEquals("2023-06-15", command.manufacturingDate());
        assertEquals("2025-06-15", command.expirationDate());
        assertEquals(250, command.quantityInStock());
        assertEquals("3.75", command.unitValue());
    }

    @Test
    @DisplayName("should map update medicine request to command")
    void shouldMapUpdateMedicineRequestToCommand() {
        // GIVEN
        var request = new UpdateMedicineRequest(
                "med-001",
                "Updated Aspirin",
                "Bayer",
                "2024-01-01",
                "2026-01-01",
                150,
                BigDecimal.valueOf(6.50)
        );

        // WHEN
        var command = medicineMapper.toUpdateCommand(request);

        // THEN
        assertNotNull(command);
        assertEquals("med-001", command.id());
        assertEquals("Updated Aspirin", command.name());
        assertEquals("Bayer", command.factoryLaboratory());
        assertEquals("2024-01-01", command.manufacturingDate());
        assertEquals("2026-01-01", command.expirationDate());
        assertEquals(150, command.quantityInStock());
        assertEquals(0, new java.math.BigDecimal("6.50").compareTo(new java.math.BigDecimal(command.unitValue())));
    }

    @Test
    @DisplayName("should handle null values in medicine response mapping")
    void shouldHandleNullValuesInMedicineResponseMapping() {
        // GIVEN
        var medicine = Medicine.create(
                "Paracetamol",
                "GSK",
                LocalDateTime.now().minusYears(2),
                LocalDateTime.now().minusDays(1),  // Expired
                500,
                Money.from(BigDecimal.valueOf(2.25))
        );

        // WHEN
        var response = medicineMapper.toResponse(medicine);

        // THEN
        assertNotNull(response);
        assertTrue(response.isExpired());
    }

    @Test
    @DisplayName("should preserve all fields in request to command mapping")
    void shouldPreserveAllFieldsInRequestToCommandMapping() {
        // GIVEN
        var request = new CreateMedicineRequest(
                "Test Medicine",
                "Test Lab",
                "2024-01-01",
                "2026-01-01",
                999,
                BigDecimal.valueOf(99.99)
        );

        // WHEN
        var command = medicineMapper.toCreateCommand(request);

        // THEN
        assertEquals(request.name(), command.name());
        assertEquals(request.factoryLaboratory(), command.factoryLaboratory());
        assertEquals(request.manufacturingDate(), command.manufacturingDate());
        assertEquals(request.expirationDate(), command.expirationDate());
        assertEquals(request.quantityInStock(), command.quantityInStock());
        assertEquals(request.unitValue().toPlainString(), command.unitValue());
    }
}
