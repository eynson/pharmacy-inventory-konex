package com.eynson.pharmacy_inventory.domain.port.in;

import com.eynson.pharmacy_inventory.domain.model.Sale;

public interface CreateSaleUseCase {
    Sale execute(CreateSaleCommand command);

    record CreateSaleCommand(
            String medicineId,
            Integer quantity
    ) {}
}
