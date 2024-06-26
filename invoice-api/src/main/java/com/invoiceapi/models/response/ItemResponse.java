package com.invoiceapi.models.response;

public record ItemResponse(
        Long id,
        String description,
        String unit,
        Double rate

) {
}
