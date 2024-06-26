package com.invoiceapi.models.response;

public record DashboardStatisticsResponse(
        long totalClients,
        double totalAmount,
        double totalDue,
        double totalPaid,
        long totalItems,
        long totalInvoices,
        long totalPaidInvoices,
        long totalUnpaidInvoices
) {
}
