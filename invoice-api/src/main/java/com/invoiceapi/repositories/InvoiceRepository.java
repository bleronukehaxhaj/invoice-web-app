package com.invoiceapi.repositories;

import com.invoiceapi.models.entities.Client;
import com.invoiceapi.models.entities.Invoice;
import com.invoiceapi.models.entities.User;
import com.invoiceapi.models.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByUser(User user);
    Page<Invoice> findAllByUser(User user, Pageable pageable);
    Page<Invoice> findAllByClient(Client client, Pageable pageable);
    Page<Invoice> findByClientNameContainingIgnoreCase(String name, Pageable pageable);

    long countByUser(User user);
    long countByUserAndInvoiceStatus(User user, InvoiceStatus status);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.client.id = :clientId AND i.user = :user")
    long countClientTotalInvoices(@Param("clientId") Long clientId, @Param("user") User user);

    @Query("SELECT SUM(i.total) FROM Invoice i WHERE i.user = :user")
    Double sumTotalAmountByUser(User user);

    @Query("SELECT SUM(i.total) FROM Invoice i WHERE i.user = :user AND i.invoiceStatus = 'PENDING'")
    Double sumTotalDueByUser(User user);

    @Query("SELECT SUM(i.total) FROM Invoice i WHERE i.user = :user AND i.invoiceStatus = 'PAID'")
    Double sumTotalPaidByUser(User user);

    @Query("SELECT SUM(i.total) FROM Invoice i WHERE i.client.id = :clientId AND i.user = :user")
    Double sumClientTotalBilled(@Param("clientId") Long clientId, @Param("user") User user);
}
