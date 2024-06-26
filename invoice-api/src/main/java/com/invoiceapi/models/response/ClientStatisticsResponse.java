package com.invoiceapi.models.response;

public record ClientStatisticsResponse(
        long clientTotalInvoices,
        double clientTotalBilled
) {
}
