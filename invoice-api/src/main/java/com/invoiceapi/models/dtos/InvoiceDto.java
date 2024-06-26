package com.invoiceapi.models.dtos;

import java.util.Date;
import java.util.List;

public record InvoiceDto(
        String invoiceNumber,
        Date issueDate,
        Date dueDate,
        String invoiceStatus,
        double discount,
        double tax,
        Long clientId,
        List<InvoiceItemDto> invoiceItems
) {
}

