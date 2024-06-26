package com.invoiceapi.services.interfaces;

import com.invoiceapi.models.dtos.ClientDto;
import com.invoiceapi.models.response.ClientResponse;
import com.invoiceapi.models.response.ItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientDto clientDto, String email);

    ClientResponse updateClient(Long id, ClientDto clientDto, String email);

    void deleteClient(Long id, String email);

    ClientResponse getClientById(Long id, String email);

    List<ClientResponse> getAllListClients(String email);

    Page<ClientResponse> getAllClients(String email, Pageable pageable);
    Page<ClientResponse> searchClientsByName(String name, String email, Pageable pageable);

}
