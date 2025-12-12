package com.eynson.pharmacy_inventory.application.service;

import com.eynson.pharmacy_inventory.application.dto.request.CreateSaleRequest;
import com.eynson.pharmacy_inventory.application.dto.request.GetSalesByDateRangeRequest;
import com.eynson.pharmacy_inventory.application.dto.response.PagedSaleResponse;
import com.eynson.pharmacy_inventory.application.dto.response.SaleResponse;
import com.eynson.pharmacy_inventory.application.mapper.SaleMapper;
import com.eynson.pharmacy_inventory.domain.port.in.CreateSaleUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.GetSalesByDateRangeUseCase;
import com.eynson.pharmacy_inventory.domain.port.in.GetSaleByIdUseCase;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleApplicationService {
    private final CreateSaleUseCase createSaleUseCase;
    private final GetSalesByDateRangeUseCase getSalesByDateRangeUseCase;
    private final GetSaleByIdUseCase getSaleByIdUseCase;
    private final SaleMapper saleMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public SaleApplicationService(
            CreateSaleUseCase createSaleUseCase,
            GetSalesByDateRangeUseCase getSalesByDateRangeUseCase,
            GetSaleByIdUseCase getSaleByIdUseCase,
            SaleMapper saleMapper) {
        this.createSaleUseCase = createSaleUseCase;
        this.getSalesByDateRangeUseCase = getSalesByDateRangeUseCase;
        this.getSaleByIdUseCase = getSaleByIdUseCase;
        this.saleMapper = saleMapper;
    }

    public SaleResponse createSale(CreateSaleRequest request) {
        var command = new CreateSaleUseCase.CreateSaleCommand(
                request.medicineId(),
                request.quantitySold()
        );
        var sale = createSaleUseCase.execute(command);
        return saleMapper.toResponse(sale);
    }

    public PagedSaleResponse getSalesByDateRange(GetSalesByDateRangeRequest request) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        
        try {
            // Intentar parsear como ISO_DATE_TIME primero
            startDate = LocalDateTime.parse(request.startDate(), DATE_FORMATTER);
        } catch (Exception e) {
            // Si falla, intentar como fecha simple (yyyy-MM-dd) y convertir a inicio del día
            startDate = java.time.LocalDate.parse(request.startDate()).atStartOfDay();
        }
        
        try {
            // Intentar parsear como ISO_DATE_TIME primero
            endDate = LocalDateTime.parse(request.endDate(), DATE_FORMATTER);
        } catch (Exception e) {
            // Si falla, intentar como fecha simple (yyyy-MM-dd) y convertir a fin del día
            endDate = java.time.LocalDate.parse(request.endDate()).atTime(23, 59, 59);
        }
        
        var query = new GetSalesByDateRangeUseCase.GetSalesByDateRangeQuery(
                startDate,
                endDate,
                request.page() != null ? request.page() : 0,
                request.pageSize() != null ? request.pageSize() : 10
        );
        var sales = getSalesByDateRangeUseCase.execute(query);
        var saleResponses = sales.stream()
                .map(saleMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedSaleResponse(
                saleResponses,
                request.page() != null ? request.page() : 0,
                request.pageSize() != null ? request.pageSize() : 10,
                (long) saleResponses.size(),
                1
        );
    }

    public SaleResponse getSaleById(String id) {
        var sale = getSaleByIdUseCase.execute(id);
        return saleMapper.toResponse(sale);
    }
}
