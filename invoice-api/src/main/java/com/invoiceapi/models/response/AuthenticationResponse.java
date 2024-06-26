package com.invoiceapi.models.response;

public record AuthenticationResponse(
        String token,
        long expiresIn
) {
}
