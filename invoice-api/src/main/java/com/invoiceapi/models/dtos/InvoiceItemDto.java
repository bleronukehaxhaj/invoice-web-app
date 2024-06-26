package com.invoiceapi.models.dtos;

public record InvoiceItemDto(
        int quantity,
        Long itemId
) {
}
