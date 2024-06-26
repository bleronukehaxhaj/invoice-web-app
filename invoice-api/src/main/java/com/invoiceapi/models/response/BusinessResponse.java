package com.invoiceapi.models.response;

public record BusinessResponse(
        Long id,
        String name,
        String address,
        String email,
        String phone
) {
}
