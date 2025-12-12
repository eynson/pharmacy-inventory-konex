package com.eynson.pharmacy_inventory.infrastructure.adapter;

import com.eynson.pharmacy_inventory.domain.model.Medicine;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import com.eynson.pharmacy_inventory.infrastructure.entity.MedicineEntity;
import com.eynson.pharmacy_inventory.infrastructure.repository.MedicineJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MedicineRepositoryAdapter implements MedicineRepositoryPort {
    private final MedicineJpaRepository medicineJpaRepository;

    public MedicineRepositoryAdapter(MedicineJpaRepository medicineJpaRepository) {
        this.medicineJpaRepository = medicineJpaRepository;
    }

    @Override
    public Medicine save(Medicine medicine) {
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
        var saved = medicineJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Medicine> findById(MedicineId id) {
        return medicineJpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<Medicine> findById(String medicineId) {
        return medicineJpaRepository.findById(medicineId)
                .map(this::toDomain);
    }

    @Override
    public PaginatedResult<Medicine> findAll(Integer page, Integer pageSize, String search, String sortBy) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var result = search != null && !search.isEmpty()
                ? medicineJpaRepository.findByNameOrFactoryLaboratory(search, pageable)
                : medicineJpaRepository.findAll(pageable);
        
        var content = result.getContent().stream()
                .map(this::toDomain)
                .toList();
        
        return new PaginatedResult<>(
                content,
                result.getTotalPages(),
                result.getTotalElements(),
                page,
                pageSize
        );
    }

    @Override
    public Optional<Medicine> findByName(String name) {
        return medicineJpaRepository.findByNameIgnoreCase(name)
                .map(this::toDomain);
    }

    @Override
    public Optional<Medicine> findByNameAndFactoryLaboratory(String name, String factoryLaboratory) {
        return medicineJpaRepository.findByNameIgnoreCaseAndFactoryLaboratoryIgnoreCase(name, factoryLaboratory)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(MedicineId id) {
        medicineJpaRepository.deleteById(id.getValue());
    }

    @Override
    public void deleteById(String medicineId) {
        medicineJpaRepository.deleteById(medicineId);
    }

    @Override
    public boolean existsById(MedicineId id) {
        return medicineJpaRepository.existsById(id.getValue());
    }

    @Override
    public long count() {
        return medicineJpaRepository.count();
    }

    private Medicine toDomain(MedicineEntity entity) {
        return Medicine.reconstruct(
                new Medicine.MedicineReconstructionData(
                        MedicineId.from(entity.getId()),
                        entity.getName(),
                        entity.getFactoryLaboratory(),
                        entity.getManufacturingDate(),
                        entity.getExpirationDate(),
                        entity.getQuantityInStock(),
                        Money.from(entity.getUnitValue()),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                )
        );
    }
}
