package com.invoiceapi.services;

import com.invoiceapi.mappers.ClientMapper;
import com.invoiceapi.models.dtos.ClientDto;
import com.invoiceapi.models.entities.Client;
import com.invoiceapi.models.entities.Item;
import com.invoiceapi.models.entities.User;
import com.invoiceapi.models.response.ClientResponse;
import com.invoiceapi.models.response.ItemResponse;
import com.invoiceapi.repositories.ClientRepository;
import com.invoiceapi.repositories.UserRepository;
import com.invoiceapi.services.interfaces.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponse createClient(ClientDto clientDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Client client = clientMapper.toEntityFromDto(clientDto);
        client.setUser(user);
        var savedClient = clientRepository.save(client);

        return clientMapper.toResponseFromEntity(savedClient);
    }

    @Override
    public ClientResponse updateClient(Long id, ClientDto clientDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!client.getUser().getEmail().equals(email)) {
            throw new SecurityException("You do not have permission to update this client");
        }
        client.setName(clientDto.name());
        client.setEmail(clientDto.email());
        client.setAddress(clientDto.address());
        client.setPhone(clientDto.phone());
        client.setClientType(clientDto.clientType());
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toResponseFromEntity(updatedClient);
    }

    @Override
    public void deleteClient(Long id, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!client.getUser().getEmail().equals(email)) {
            throw new SecurityException("You do not have permission to update this client");
        }
        clientRepository.delete(client);
    }

    @Override
    public ClientResponse getClientById(Long id, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!client.getUser().getEmail().equals(email)) {
            throw new SecurityException("You do not have permission to update this client");
        }
        return clientMapper.toResponseFromEntity(client);
    }

    @Override
    public List<ClientResponse> getAllListClients(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Client> clients = clientRepository.findAllByUser(user);
        return clients.stream()
                .map(clientMapper::toResponseFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ClientResponse> getAllClients(String email, Pageable pageable) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Page<Client> clients = clientRepository.findAllByUser(user, pageable);
        return clients.map(clientMapper::toResponseFromEntity);
    }

    @Override
    public Page<ClientResponse> searchClientsByName(String name, String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Page<Client> clients = clientRepository.findByNameContainingIgnoreCase(name, pageable);

        List<ClientResponse> filteredClients = clients.getContent().stream()
                .filter(client -> client.getUser().equals(user))
                .map(clientMapper::toResponseFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(filteredClients, pageable, clients.getTotalElements());
    }
}
