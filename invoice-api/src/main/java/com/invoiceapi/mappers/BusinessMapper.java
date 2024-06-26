package com.invoiceapi.mappers;

import com.invoiceapi.models.dtos.BusinessDto;
import com.invoiceapi.models.entities.Business;
import com.invoiceapi.models.response.BusinessResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BusinessMapper extends Convert<Business, BusinessDto, BusinessResponse> {
    BusinessMapper MAPPER = Mappers.getMapper(BusinessMapper.class);
}
