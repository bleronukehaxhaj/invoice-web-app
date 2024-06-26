package com.invoiceapi.mappers;

import com.invoiceapi.models.dtos.ItemDto;
import com.invoiceapi.models.entities.Item;
import com.invoiceapi.models.response.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemMapper extends Convert<Item, ItemDto, ItemResponse>{

    ItemMapper MAPPER = Mappers.getMapper(ItemMapper.class);
}
