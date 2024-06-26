package com.invoiceapi.models.dtos;

import com.invoiceapi.models.enums.ClientType;


public record ClientDto(
        String name,
        String email,
        String address,
        String phone,
        ClientType clientType
) {
}
