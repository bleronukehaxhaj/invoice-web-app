package com.invoiceapi.controllers;

import com.invoiceapi.models.dtos.ClientDto;
import com.invoiceapi.models.response.ClientResponse;
import com.invoiceapi.services.interfaces.ClientService;
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
@RequestMapping("/api/v1/clients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientDto clientDto,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        ClientResponse clientResponse = clientService.createClient(clientDto, userDetails.getUsername());
        return ResponseEntity.ok(clientResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id,
                                                       @RequestBody ClientDto clientDto,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        ClientResponse clientResponse = clientService.updateClient(id, clientDto, userDetails.getUsername());
        return ResponseEntity.ok(clientResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        clientService.deleteClient(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        ClientResponse clientResponse = clientService.getClientById(id, userDetails.getUsername());
        return ResponseEntity.ok(clientResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClientResponse>> getAllListClients(@AuthenticationPrincipal UserDetails userDetails) {
        List<ClientResponse> clientResponses = clientService.getAllListClients(userDetails.getUsername());
        return ResponseEntity.ok(clientResponses);
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getAllClients(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int sizePerPage,
                                                              @RequestParam(defaultValue = "updatedAt") String sortBy,
                                                              @RequestParam(defaultValue = "desc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, sizePerPage, Sort.by(direction, sortBy));
        Page<ClientResponse> clientResponses = clientService.getAllClients(userDetails.getUsername(), pageable);
        return ResponseEntity.ok(clientResponses);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ClientResponse>> searchClientsByName(
            @RequestParam String name,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int sizePerPage) {

        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = userDetails.getUsername();
        Pageable pageable = PageRequest.of(page, sizePerPage);
        Page<ClientResponse> clients = clientService.searchClientsByName(name, email, pageable);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
