package com.invoiceapi.repositories;

import com.invoiceapi.models.entities.Invoice;
import com.invoiceapi.models.entities.Item;
import com.invoiceapi.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUser(User user);
    Page<Item> findAllByUser(User user, Pageable pageable);
    Page<Item> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    long countByUser(User user);

}
