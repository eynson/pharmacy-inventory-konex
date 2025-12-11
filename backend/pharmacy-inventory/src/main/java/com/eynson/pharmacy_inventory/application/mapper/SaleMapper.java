package com.eynson.pharmacy_inventory.application.mapper;

import com.eynson.pharmacy_inventory.application.dto.response.SaleResponse;
import com.eynson.pharmacy_inventory.domain.model.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

    public SaleResponse toResponse(Sale sale) {
        return new SaleResponse(
                sale.getId().getValue(),
                sale.getMedicineId().getValue(),
                sale.getMedicineName(),
                sale.getQuantitySold().getValue(),
                sale.getUnitValue().getAmount(),
                sale.getTotalValue().getAmount(),
                sale.getSaleDateTime()
        );
    }
}
