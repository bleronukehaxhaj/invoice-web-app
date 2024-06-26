package com.invoiceapi.models.dtos;

public record ItemDto(
        String description,
        String unit,
        Double rate
) {
}
