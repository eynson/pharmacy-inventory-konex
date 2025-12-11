package com.eynson.pharmacy_inventory.infrastructure.adapter;

import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.model.SaleId;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;
import com.eynson.pharmacy_inventory.infrastructure.entity.SaleEntity;
import com.eynson.pharmacy_inventory.infrastructure.repository.SaleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Component
public class SaleRepositoryAdapter implements SaleRepositoryPort {
    private final SaleJpaRepository saleJpaRepository;

    public SaleRepositoryAdapter(SaleJpaRepository saleJpaRepository) {
        this.saleJpaRepository = saleJpaRepository;
    }

    @Override
    public Sale save(Sale sale) {
        var entity = new SaleEntity(
                sale.getId().getValue(),
                sale.getMedicineId().getValue(),
                sale.getMedicineName(),
                sale.getQuantitySold().getValue(),
                sale.getUnitValue().getAmount(),
                sale.getTotalValue().getAmount(),
                sale.getSaleDateTime(),
                sale.getCreatedAt()
        );
        var saved = saleJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Sale> findById(SaleId id) {
        return saleJpaRepository.findById(id.getValue())
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
                .collect(Collectors.toList());
    }

    @Override
    public void delete(SaleId id) {
        saleJpaRepository.deleteById(id.getValue());
    }

    @Override
    public long count() {
        return saleJpaRepository.count();
    }

    @Override
    public long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleJpaRepository.findByDateRange(startDate, endDate).size();
    }

    @Override
    public List<Sale> findByMedicineId(String medicineId) {
        return saleJpaRepository.findByMedicineId(medicineId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Sale toDomain(SaleEntity entity) {
        return Sale.reconstruct(
                SaleId.from(entity.getId()),
                com.eynson.pharmacy_inventory.domain.model.MedicineId.from(entity.getMedicineId()),
                entity.getMedicineName(),
                entity.getQuantitySold(),
                new com.eynson.pharmacy_inventory.domain.model.Money(entity.getUnitValue()),
                new com.eynson.pharmacy_inventory.domain.model.Money(entity.getTotalValue()),
                entity.getSaleDateTime(),
                entity.getCreatedAt()
        );
    }
}
