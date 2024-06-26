package com.invoiceapi.models.response;

import com.invoiceapi.models.enums.ClientType;

public record ClientResponse(
        Long id,
        String name,
        String email,
        String address,
        String phone,
        ClientType clientType
) {
}
