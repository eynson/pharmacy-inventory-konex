package com.eynson.pharmacy_inventory.infrastructure.config;

import com.eynson.pharmacy_inventory.domain.port.in.*;
import com.eynson.pharmacy_inventory.domain.port.out.MedicineRepositoryPort;
import com.eynson.pharmacy_inventory.domain.port.out.SaleRepositoryPort;
import com.eynson.pharmacy_inventory.domain.usecase.medicine.*;
import com.eynson.pharmacy_inventory.domain.usecase.sale.CreateSaleUseCaseImpl;
import com.eynson.pharmacy_inventory.domain.usecase.sale.GetSalesByDateRangeUseCaseImpl;
import com.eynson.pharmacy_inventory.domain.usecase.sale.GetSaleByIdUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateMedicineUseCase createMedicineUseCase(MedicineRepositoryPort medicineRepository) {
        return new CreateMedicineUseCaseImpl(medicineRepository);
    }

    @Bean
    public UpdateMedicineUseCase updateMedicineUseCase(MedicineRepositoryPort medicineRepository) {
        return new UpdateMedicineUseCaseImpl(medicineRepository);
    }

    @Bean
    public DeleteMedicineUseCase deleteMedicineUseCase(MedicineRepositoryPort medicineRepository) {
        return new DeleteMedicineUseCaseImpl(medicineRepository);
    }

    @Bean
    public GetMedicineByIdUseCase getMedicineByIdUseCase(MedicineRepositoryPort medicineRepository) {
        return new GetMedicineByIdUseCaseImpl(medicineRepository);
    }

    @Bean
    public GetMedicinesUseCase getMedicinesUseCase(MedicineRepositoryPort medicineRepository) {
        return new GetMedicinesUseCaseImpl(medicineRepository);
    }

    @Bean
    public CreateSaleUseCase createSaleUseCase(MedicineRepositoryPort medicineRepository,
                                               SaleRepositoryPort saleRepository) {
        return new CreateSaleUseCaseImpl(medicineRepository, saleRepository);
    }

    @Bean
    public GetSalesByDateRangeUseCase getSalesByDateRangeUseCase(SaleRepositoryPort saleRepository) {
        return new GetSalesByDateRangeUseCaseImpl(saleRepository);
    }

    @Bean
    public GetSaleByIdUseCase getSaleByIdUseCase(SaleRepositoryPort saleRepository) {
        return new GetSaleByIdUseCaseImpl(saleRepository);
    }
}
