package com.eynson.pharmacy_inventory.infrastructure.repository;

import com.eynson.pharmacy_inventory.infrastructure.entity.SaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleJpaRepository extends JpaRepository<SaleEntity, String> {
    @Query("SELECT s FROM SaleEntity s WHERE " +
           "s.saleDateTime >= :startDate AND s.saleDateTime <= :endDate " +
           "ORDER BY s.saleDateTime DESC")
    List<SaleEntity> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<SaleEntity> findByMedicineId(String medicineId);
}
