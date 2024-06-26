package com.invoiceapi.services.interfaces;

import com.invoiceapi.models.response.ClientStatisticsResponse;
import com.invoiceapi.models.response.DashboardStatisticsResponse;

public interface DashboardService {
    DashboardStatisticsResponse getDashboardStatistics(String email);
    ClientStatisticsResponse getClientStatistics(Long clientId, String email);
}
