package com.eynson.pharmacy_inventory.infrastructure.adapter;

import com.eynson.pharmacy_inventory.domain.model.Money;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.model.SaleId;
import com.eynson.pharmacy_inventory.domain.model.MedicineId;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;
import com.eynson.pharmacy_inventory.infrastructure.entity.SaleEntity;
import com.eynson.pharmacy_inventory.infrastructure.repository.SaleJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SaleRepositoryAdapter implements SaleRepositoryPort {
    private final SaleJpaRepository saleJpaRepository;

    public SaleRepositoryAdapter(SaleJpaRepository saleJpaRepository) {
        this.saleJpaRepository = saleJpaRepository;
    }

    @Override
    public Sale save(Sale sale) {
        var entity = SaleEntity.builder()
                .id(sale.getId().getValue())
                .medicineId(sale.getMedicineId().getValue())
                .medicineName(sale.getMedicineName())
                .quantitySold(sale.getQuantitySold().getValue())
                .unitValue(sale.getUnitValue().getAmount())
                .totalValue(sale.getTotalValue().getAmount())
                .saleDateTime(sale.getSaleDateTime())
                .createdAt(sale.getCreatedAt())
                .build();
        var saved = saleJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Sale> findById(SaleId id) {
        return saleJpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public Optional<Sale> findById(String saleId) {
        return saleJpaRepository.findById(saleId)
                .map(this::toDomain);
    }

    @Override
    public List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                      Integer page, Integer pageSize) {
        return saleJpaRepository.findByDateRange(startDate, endDate)
                .stream()
                .skip((long) page * pageSize)
                .limit(pageSize)
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(SaleId id) {
        saleJpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return saleJpaRepository.count();
    }

    @Override
    public SaleRepositoryPort.PaginatedResult<Sale> findAll(Integer page, Integer pageSize) {
        var result = saleJpaRepository.findAll(
                org.springframework.data.domain.PageRequest.of(page, pageSize)
        );
        return new SaleRepositoryPort.PaginatedResult<>(
                result.getContent().stream().map(this::toDomain).toList(),
                result.getTotalPages(),
                result.getTotalElements(),
                page,
                pageSize
        );
    }

    private Sale toDomain(SaleEntity entity) {
        return Sale.reconstruct(
                new Sale.SaleReconstructionData(
                        SaleId.from(entity.getId()),
                        MedicineId.from(entity.getMedicineId()),
                        entity.getMedicineName(),
                        entity.getQuantitySold(),
                        Money.from(entity.getUnitValue()),
                        Money.from(entity.getTotalValue()),
                        entity.getSaleDateTime(),
                        entity.getCreatedAt()
                )
        );
    }
}
