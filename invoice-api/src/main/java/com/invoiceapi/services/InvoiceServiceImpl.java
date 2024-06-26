package com.invoiceapi.services;

import com.invoiceapi.mappers.InvoiceItemMapper;
import com.invoiceapi.mappers.InvoiceMapper;
import com.invoiceapi.models.dtos.InvoiceDto;
import com.invoiceapi.models.dtos.InvoiceItemDto;
import com.invoiceapi.models.entities.*;
import com.invoiceapi.models.enums.InvoiceStatus;
import com.invoiceapi.models.response.InvoiceResponse;
import com.invoiceapi.repositories.*;
import com.invoiceapi.services.interfaces.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;


    @Override
    public InvoiceResponse createInvoice(InvoiceDto invoiceDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var business = businessRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));

        Invoice invoice = invoiceMapper.toEntityFromDto(invoiceDto);
        invoice.setUser(user);
        invoice.setBusiness(business);

        Client client = clientRepository.findById(invoiceDto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        invoice.setClient(client);


        List<InvoiceItem> invoiceItems = new ArrayList<>();

        for (InvoiceItemDto itemDTO : invoiceDto.invoiceItems()) {
            InvoiceItem invoiceItem = invoiceItemMapper.toEntity(itemDTO);

            var item = itemRepository.findById(itemDTO.itemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));

            invoiceItem.setItem(item);
            invoiceItem.setItemRate(item.getRate());
            invoiceItem.setTotalPrice(itemDTO.quantity() * item.getRate());
            invoiceItem.setInvoice(invoice);

            invoiceItems.add(invoiceItem);
        }
        invoice.setInvoiceItems(invoiceItems);


        double subtotal = invoiceItems.stream()
                .mapToDouble(InvoiceItem::getTotalPrice)
                .sum();
        double discountPercentage = invoiceDto.discount();
        double taxPercentage = invoiceDto.tax();
        double discountAmount = subtotal * (discountPercentage / 100);
        double taxAmount = (subtotal - discountAmount) * (taxPercentage / 100);
        double total = subtotal - discountAmount + taxAmount;

        invoice.setSubtotal(subtotal);
        invoice.setTotal(total);

        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toResponseFromEntity(invoice);
    }

    @Override
    public InvoiceResponse getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
        return invoiceMapper.toResponseFromEntity(invoice);
    }

    @Transactional
    @Override
    public InvoiceResponse updateInvoice(Long id, InvoiceDto invoiceDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));


        Business business = businessRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));
        invoice.setBusiness(business);


        invoice.setInvoiceNumber(invoiceDto.invoiceNumber());
        invoice.setIssueDate(invoiceDto.issueDate());
        invoice.setDueDate(invoiceDto.dueDate());
        invoice.setInvoiceStatus(InvoiceStatus.valueOf(invoiceDto.invoiceStatus()));

        Client client = clientRepository.findById(invoiceDto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        invoice.setClient(client);


        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (InvoiceItemDto itemDTO : invoiceDto.invoiceItems()) {
            InvoiceItem invoiceItem = invoiceItemMapper.toEntity(itemDTO);

            var item = itemRepository.findById(itemDTO.itemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found"));
            invoiceItem.setItem(item);
            invoiceItem.setItemRate(item.getRate());
            invoiceItem.setTotalPrice(itemDTO.quantity() * item.getRate());
            invoiceItem.setInvoice(invoice);

            invoiceItems.add(invoiceItem);
        }
        invoice.getInvoiceItems().clear();
        invoice.getInvoiceItems().addAll(invoiceItems);


        double subtotal = invoiceItems.stream()
                .mapToDouble(InvoiceItem::getTotalPrice)
                .sum();
        double discountPercentage = invoiceDto.discount();
        double taxPercentage = invoiceDto.tax();
        double discountAmount = subtotal * (discountPercentage / 100);
        double taxAmount = (subtotal - discountAmount) * (taxPercentage / 100);
        double total = subtotal - discountAmount + taxAmount;

        invoice.setSubtotal(subtotal);
        invoice.setDiscount(discountPercentage);
        invoice.setTax(taxPercentage);
        invoice.setTotal(total);

        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toResponseFromEntity(invoice);
    }

    @Override
    public void deleteInvoice(Long id, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!invoice.getUser().equals(user)) {
            throw new SecurityException("You do not have permission to update this client");
        }
        invoiceRepository.delete(invoice);
    }

    @Override
    public List<InvoiceResponse> getAllInvoices(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Invoice> invoices = invoiceRepository.findAllByUser(user);

        return invoices.stream()
                .map(invoiceMapper::toResponseFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void updateInvoiceStatus(Long invoiceId, String status, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        if (!invoice.getUser().equals(user)) {
            throw new SecurityException("You do not have permission to update this invoice");
        }

        InvoiceStatus invoiceStatus;
        try {
            invoiceStatus = InvoiceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value");
        }

        invoice.setInvoiceStatus(invoiceStatus);
        invoiceRepository.save(invoice);
    }

    @Override
    public Page<InvoiceResponse> getInvoices(String email, Pageable pageable) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Page<Invoice> invoices = invoiceRepository.findAllByUser(user, pageable);

        return invoices.map(invoiceMapper::toResponseFromEntity);
    }


    @Override
    public Page<InvoiceResponse> getInvoicesByClient(Long clientId, String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Page<Invoice> invoices = invoiceRepository.findAllByClient(client, pageable);

        return invoices.map(invoiceMapper::toResponseFromEntity);
    }

    @Override
    public Page<InvoiceResponse> searchInvoicesByClientName(String clientName, String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Page<Invoice> invoices = invoiceRepository.findByClientNameContainingIgnoreCase(clientName, pageable);

        List<InvoiceResponse> filteredInvoices = invoices.getContent().stream()
                .filter(invoice -> invoice.getUser().equals(user))
                .map(invoiceMapper::toResponseFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(filteredInvoices, pageable, invoices.getTotalElements());
    }
}
