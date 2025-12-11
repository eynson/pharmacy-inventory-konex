package com.eynson.pharmacy_inventory.domain.port.in;

import com.eynson.pharmacy_inventory.domain.model.Sale;
import java.time.LocalDateTime;
import java.util.List;

public interface GetSalesByDateRangeUseCase {
    List<Sale> execute(GetSalesByDateRangeQuery query);

    record GetSalesByDateRangeQuery(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer page,
            Integer pageSize
    ) {}
}
