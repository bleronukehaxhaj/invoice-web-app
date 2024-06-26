package com.invoiceapi.repositories;

import com.invoiceapi.models.entities.Business;
import com.invoiceapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByUser(User user);
}
