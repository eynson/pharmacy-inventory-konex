package com.eynson.pharmacy_inventory.domain.usecase.sale;

import com.eynson.pharmacy_inventory.domain.exception.SaleNotFoundException;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.port.in.GetSaleByIdUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;

public class GetSaleByIdUseCaseImpl implements GetSaleByIdUseCase {
    private final SaleRepositoryPort saleRepository;

    public GetSaleByIdUseCaseImpl(SaleRepositoryPort saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale execute(String id) {
        validateId(id);

        return saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("No se encontró la venta con id: " + id));
    }

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id de la venta es requerido");
        }
    }
}
