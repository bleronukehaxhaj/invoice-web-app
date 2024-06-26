package com.invoiceapi.mappers;

import com.invoiceapi.models.dtos.InvoiceItemDto;
import com.invoiceapi.models.entities.InvoiceItem;
import com.invoiceapi.models.entities.Item;
import com.invoiceapi.models.response.InvoiceItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvoiceItemMapper {
    InvoiceItemMapper MAPPER = Mappers.getMapper(InvoiceItemMapper.class);

    @Mapping(source = "item", target = "item")
    InvoiceItemResponse toResponse(InvoiceItem invoiceItem);

    @Mapping(target = "itemRate", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "item", source = "itemId", qualifiedByName = "itemFromId")
    InvoiceItem toEntity(InvoiceItemDto invoiceItemDto);

    @Named("itemFromId")
    default Item itemFromId(Long itemId) {
        if (itemId == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemId);
        return item;
    }
}
