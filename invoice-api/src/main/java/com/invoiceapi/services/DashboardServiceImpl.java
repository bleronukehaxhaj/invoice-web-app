package com.invoiceapi.services;

import com.invoiceapi.models.enums.InvoiceStatus;
import com.invoiceapi.models.response.ClientStatisticsResponse;
import com.invoiceapi.models.response.DashboardStatisticsResponse;
import com.invoiceapi.repositories.ClientRepository;
import com.invoiceapi.repositories.InvoiceRepository;
import com.invoiceapi.repositories.ItemRepository;
import com.invoiceapi.repositories.UserRepository;
import com.invoiceapi.services.interfaces.DashboardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;


    @Override
    public DashboardStatisticsResponse getDashboardStatistics(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        long totalClients = clientRepository.countByUser(user);
        long totalItems = itemRepository.countByUser(user);
        long totalInvoices = invoiceRepository.countByUser(user);
        long totalPaidInvoices = invoiceRepository.countByUserAndInvoiceStatus(user, InvoiceStatus.PAID);
        long totalUnpaidInvoices = invoiceRepository.countByUserAndInvoiceStatus(user, InvoiceStatus.PENDING);

        double totalAmount = invoiceRepository.sumTotalAmountByUser(user) != null ? invoiceRepository.sumTotalAmountByUser(user) : 0.0;
        double totalDue = invoiceRepository.sumTotalDueByUser(user) != null ? invoiceRepository.sumTotalDueByUser(user) : 0.0;
        double totalPaid = invoiceRepository.sumTotalPaidByUser(user) != null ? invoiceRepository.sumTotalPaidByUser(user) : 0.0;

        return new DashboardStatisticsResponse(
                totalClients,
                totalAmount,
                totalDue,
                totalPaid,
                totalItems,
                totalInvoices,
                totalPaidInvoices,
                totalUnpaidInvoices
        );
    }

    @Override
    public ClientStatisticsResponse getClientStatistics(Long clientId, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        long clientTotalInvoices = invoiceRepository.countClientTotalInvoices(clientId, user);
        double clientTotalBilled = invoiceRepository.sumClientTotalBilled(clientId, user) != null ? invoiceRepository.sumClientTotalBilled(clientId, user) : 0.0;

        return new ClientStatisticsResponse(clientTotalInvoices, clientTotalBilled);
    }
}
