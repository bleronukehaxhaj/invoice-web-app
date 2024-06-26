package com.invoiceapi.mappers;

import com.invoiceapi.models.dtos.ClientDto;
import com.invoiceapi.models.entities.Client;
import com.invoiceapi.models.response.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper extends Convert<Client, ClientDto, ClientResponse> {
    ClientMapper MAPPER = Mappers.getMapper(ClientMapper.class);
}
