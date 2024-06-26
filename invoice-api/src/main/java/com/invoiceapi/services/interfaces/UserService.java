package com.invoiceapi.services.interfaces;

import com.invoiceapi.models.response.UserResponse;

public interface UserService {
    UserResponse getUserDetails(String email);
}
