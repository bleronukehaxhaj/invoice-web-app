package com.invoiceapi.services;

import com.invoiceapi.models.response.UserResponse;
import com.invoiceapi.repositories.UserRepository;
import com.invoiceapi.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse getUserDetails(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
