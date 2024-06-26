package com.invoiceapi.models.response;

import com.invoiceapi.models.entities.Business;
import com.invoiceapi.models.entities.Client;
import com.invoiceapi.models.enums.InvoiceStatus;

import java.util.Date;
import java.util.List;

public record InvoiceResponse(
        Long id,
        String invoiceNumber,
        Date issueDate,
        Date dueDate,
        InvoiceStatus invoiceStatus,
        double subtotal,
        double discount,
        double tax,
        double total,
        BusinessResponse business,
        ClientResponse client,
        List<InvoiceItemResponse> invoiceItems
) {
}
