package com.invoiceapi.controllers;

import com.invoiceapi.models.dtos.InvoiceDto;
import com.invoiceapi.models.response.InvoiceResponse;
import com.invoiceapi.models.response.UpdateInvoiceStatusRequest;
import com.invoiceapi.services.interfaces.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody InvoiceDto invoiceDto,
                                                         @AuthenticationPrincipal UserDetails userDetails) {

        InvoiceResponse createdInvoice = invoiceService.createInvoice(invoiceDto, userDetails.getUsername());
        return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        InvoiceResponse invoice = invoiceService.getInvoiceById(id);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id,
                                                         @RequestBody InvoiceDto invoiceDto,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        InvoiceResponse updatedInvoice = invoiceService.updateInvoice(id, invoiceDto, userDetails.getUsername());
        return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        invoiceService.deleteInvoice(id, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices(@AuthenticationPrincipal UserDetails userDetails) {
        List<InvoiceResponse> invoices = invoiceService.getAllInvoices(userDetails.getUsername());
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<InvoiceResponse>> getInvoices(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "5") int sizePerPage,
                                                             @RequestParam(defaultValue = "updatedAt") String sortBy,
                                                             @RequestParam(defaultValue = "desc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, sizePerPage, Sort.by(direction, sortBy));
        Page<InvoiceResponse> invoices = invoiceService.getInvoices(userDetails.getUsername(), pageable);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<InvoiceResponse>> getInvoicesByClient(@PathVariable Long clientId,
                                                                     @AuthenticationPrincipal UserDetails userDetails,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "5") int sizePerPage,
                                                                     @RequestParam(defaultValue = "updatedAt") String sortBy,
                                                                     @RequestParam(defaultValue = "desc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, sizePerPage, Sort.by(direction, sortBy));
        Page<InvoiceResponse> invoices = invoiceService.getInvoicesByClient(clientId, userDetails.getUsername(), pageable);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<InvoiceResponse>> searchInvoicesByClientName(
            @RequestParam String clientName,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int sizePerPage) {

        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = userDetails.getUsername();
        Pageable pageable = PageRequest.of(page, sizePerPage);
        Page<InvoiceResponse> invoices = invoiceService.searchInvoicesByClientName(clientName, email, pageable);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @PatchMapping("/{invoiceId}/status")
    public ResponseEntity<Void> updateInvoiceStatus(
            @PathVariable Long invoiceId,
            @RequestBody UpdateInvoiceStatusRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = userDetails.getUsername();
        invoiceService.updateInvoiceStatus(invoiceId, request.status(), email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
