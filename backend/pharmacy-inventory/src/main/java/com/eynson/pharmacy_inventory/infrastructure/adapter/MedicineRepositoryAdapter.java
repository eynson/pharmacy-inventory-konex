package com.eynson.pharmacy_inventory.infrastructure.adapter;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import com.eynson.pharmacy_inventory.infrastructure.entity.MedicineEntity;
import com.eynson.pharmacy_inventory.infrastructure.repository.MedicineJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MedicineRepositoryAdapter implements MedicineRepositoryPort {
    private final MedicineJpaRepository medicineJpaRepository;

    public MedicineRepositoryAdapter(MedicineJpaRepository medicineJpaRepository) {
        this.medicineJpaRepository = medicineJpaRepository;
    }

    @Override
    public Medicine save(Medicine medicine) {
        var entity = new MedicineEntity(
                medicine.getId().getValue(),
                medicine.getName(),
                medicine.getFactoryLaboratory(),
                medicine.getManufacturingDate(),
                medicine.getExpirationDate(),
                medicine.getQuantityInStock().getValue(),
                medicine.getUnitValue().getAmount(),
                medicine.getCreatedAt(),
                medicine.getUpdatedAt()
        );
        var saved = medicineJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Medicine> findById(MedicineId id) {
        return medicineJpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public List<Medicine> findAll(Integer page, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var result = search != null && !search.isEmpty()
                ? medicineJpaRepository.findByNameOrFactoryLaboratory(search, pageable)
                : medicineJpaRepository.findAll(pageable);
        return result.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(MedicineId id) {
        medicineJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean exists(MedicineId id) {
        return medicineJpaRepository.existsById(id.getValue());
    }

    @Override
    public long count() {
        return medicineJpaRepository.count();
    }

    @Override
    public long countBySearch(String search) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        return medicineJpaRepository.findByNameOrFactoryLaboratory(search, pageable).getTotalElements();
    }

    @Override
    public boolean existsByName(String name) {
        return medicineJpaRepository.findAll().stream()
                .anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    private Medicine toDomain(MedicineEntity entity) {
        return Medicine.reconstruct(
                MedicineId.from(entity.getId()),
                entity.getName(),
                entity.getFactoryLaboratory(),
                entity.getManufacturingDate(),
                entity.getExpirationDate(),
                entity.getQuantityInStock(),
                new com.eynson.pharmacy_inventory.domain.model.Money(entity.getUnitValue()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
