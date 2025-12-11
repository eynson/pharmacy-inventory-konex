package com.eynson.pharmacy_inventory.domain.port.out;

import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.model.SaleId;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface SaleRepositoryPort {
    /**
     * Guarda una nueva venta.
     */
    Sale save(Sale sale);

    /**
     * Obtiene una venta por su ID.
     */
    Optional<Sale> findById(SaleId saleId);

    /**
     * Obtiene una venta por su ID en formato String.
     */
    Optional<Sale> findById(String saleId);

    /**
     * Obtiene todas las ventas en un rango de fechas.
     */
    List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer pageSize);

    /**
     * Obtiene todas las ventas con paginación.
     */
    PaginatedResult<Sale> findAll(Integer page, Integer pageSize);

    /**
     * Elimina una venta por su ID.
     */
    void deleteById(SaleId saleId);

    /**
     * Obtiene el total de ventas.
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
