package com.eynson.pharmacy_inventory.application.dto.request;

public record GetMedicinesRequest(
        Integer page,
        Integer pageSize,
        String search
) {
    public GetMedicinesRequest {
        if (page == null || page < 0) {
            page = 0;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
    }
}
