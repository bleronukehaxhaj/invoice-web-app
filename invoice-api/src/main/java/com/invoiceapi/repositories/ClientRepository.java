package com.invoiceapi.repositories;

import com.invoiceapi.models.entities.Client;
import com.invoiceapi.models.entities.Item;
import com.invoiceapi.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByUser(User user);
    Page<Client> findAllByUser(User user, Pageable pageable);
    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);
    long countByUser(User user);
}
