package com.invoiceapi.services.interfaces;

import com.invoiceapi.models.dtos.InvoiceDto;
import com.invoiceapi.models.entities.Client;
import com.invoiceapi.models.entities.Invoice;
import com.invoiceapi.models.response.BusinessResponse;
import com.invoiceapi.models.response.InvoiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvoiceService {
    InvoiceResponse createInvoice(InvoiceDto invoiceDto, String email);

    InvoiceResponse getInvoiceById(Long invoiceId);

    InvoiceResponse updateInvoice(Long id, InvoiceDto invoiceDto, String email);

    void deleteInvoice(Long id, String email);

    List<InvoiceResponse> getAllInvoices(String email);
    void updateInvoiceStatus(Long invoiceId, String status, String email);

    Page<InvoiceResponse> getInvoices(String email, Pageable pageable);
    Page<InvoiceResponse> getInvoicesByClient(Long clientId, String email, Pageable pageable);
    Page<InvoiceResponse> searchInvoicesByClientName(String clientName, String email, Pageable pageable);
}
