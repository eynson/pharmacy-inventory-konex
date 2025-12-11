package com.eynson.pharmacy_inventory.domain.usecase.sale;

import com.eynson.pharmacy_inventory.domain.model.Sale;
import com.eynson.pharmacy_inventory.domain.port.in.GetSalesByDateRangeUseCase;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;
import java.util.List;

public class GetSalesByDateRangeUseCaseImpl implements GetSalesByDateRangeUseCase {
    private final SaleRepositoryPort saleRepository;

    public GetSalesByDateRangeUseCaseImpl(SaleRepositoryPort saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sale> execute(GetSalesByDateRangeQuery query) {
        validateQuery(query);

        return saleRepository.findByDateRange(
                query.startDate(),
                query.endDate(),
                query.page(),
                query.pageSize()
        );
    }

    private void validateQuery(GetSalesByDateRangeQuery query) {
        if (query.startDate() == null) {
            throw new IllegalArgumentException("La fecha de inicio es requerida");
        }
        if (query.endDate() == null) {
            throw new IllegalArgumentException("La fecha de fin es requerida");
        }
        if (query.endDate().isBefore(query.startDate())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        if (query.page() == null || query.page() < 0) {
            throw new IllegalArgumentException("El número de página debe ser mayor o igual a 0");
        }
        if (query.pageSize() == null || query.pageSize() <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor a 0");
        }
    }
}
