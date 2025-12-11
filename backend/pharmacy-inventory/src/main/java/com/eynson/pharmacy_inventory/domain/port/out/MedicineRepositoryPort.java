package com.eynson.pharmacy_inventory.domain.port.out;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import java.util.Optional;
import java.util.List;

public interface MedicineRepositoryPort {
    /**
     * Guarda una nueva medicina o actualiza una existente.
     */
    Medicine save(Medicine medicine);

    /**
     * Obtiene una medicina por su ID.
     */
    Optional<Medicine> findById(MedicineId medicineId);

    /**
     * Obtiene una medicina por su ID en formato String.
     */
    Optional<Medicine> findById(String medicineId);

    /**
     * Obtiene todas las medicinas con paginación y filtrado.
     */
    PaginatedResult<Medicine> findAll(Integer page, Integer pageSize, String search, String sortBy);

    /**
     * Elimina una medicina por su ID.
     */
    void deleteById(MedicineId medicineId);

    /**
     * Elimina una medicina por su ID en formato String.
     */
    void deleteById(String medicineId);

    /**
     * Verifica si existe una medicina con el ID especificado.
     */
    boolean existsById(MedicineId medicineId);

    /**
     * Obtiene el total de medicinas.
     */
    long count();

    record PaginatedResult<T>(
            List<T> content,
            Integer totalPages,
            Long totalElements,
            Integer currentPage,
            Integer pageSize
    ) {}
}
