package com.invoiceapi.mappers;

import com.invoiceapi.models.dtos.InvoiceDto;
import com.invoiceapi.models.entities.Invoice;
import com.invoiceapi.models.response.InvoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {InvoiceItemMapper.class, BusinessMapper.class, ClientMapper.class})
public interface InvoiceMapper extends Convert<Invoice, InvoiceDto, InvoiceResponse> {
    InvoiceMapper MAPPER = Mappers.getMapper(InvoiceMapper.class);
}
