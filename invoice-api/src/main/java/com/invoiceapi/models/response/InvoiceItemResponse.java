package com.invoiceapi.models.response;



public record InvoiceItemResponse(
        Long id,
        int quantity,
        double totalPrice,
        double itemRate,
        ItemResponse item
) {
}
