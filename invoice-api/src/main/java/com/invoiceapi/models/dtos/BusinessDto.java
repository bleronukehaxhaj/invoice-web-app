package com.invoiceapi.models.dtos;

public record BusinessDto(
        String name,
        String address,
        String email,
        String phone
) {
}
