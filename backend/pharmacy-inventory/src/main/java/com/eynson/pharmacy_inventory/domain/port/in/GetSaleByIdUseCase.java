package com.eynson.pharmacy_inventory.domain.port.in;

import com.eynson.pharmacy_inventory.domain.model.Sale;

public interface GetSaleByIdUseCase {
    Sale execute(String id);
}
