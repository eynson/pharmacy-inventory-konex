package com.eynson.pharmacy_inventory.infrastructure.repository;

import com.eynson.pharmacy_inventory.infrastructure.entity.MedicineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineJpaRepository extends JpaRepository<MedicineEntity, String> {
    @Query(nativeQuery = true, value = """
    SELECT m.* FROM medicines m
    WHERE LOWER(m.name) LIKE LOWER('%' || :search || '%')
       OR LOWER(m.factory_laboratory) LIKE LOWER('%' || :search || '%')
""")
    Page<MedicineEntity> findByNameOrFactoryLaboratory(@Param("search") String search, Pageable pageable);

    Optional<MedicineEntity> findByNameIgnoreCase(String name);

    Optional<MedicineEntity> findByNameIgnoreCaseAndFactoryLaboratoryIgnoreCase(String name, String factoryLaboratory);

    Page<MedicineEntity> findAll(Pageable pageable);
}
