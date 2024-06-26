package com.invoiceapi.models.response;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {

}