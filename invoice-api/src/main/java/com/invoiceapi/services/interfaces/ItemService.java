package com.invoiceapi.services.interfaces;

import com.invoiceapi.models.dtos.ItemDto;
import com.invoiceapi.models.response.ItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ItemService {
    ItemResponse createItem(ItemDto itemDto, String email);

    ItemResponse updateItem(Long id, ItemDto itemDto, String email);

    void deleteItem(Long id, String email);

    ItemResponse getItemById(Long id, String email);

    List<ItemResponse> getAllListItems(String email);

    Page<ItemResponse> getAllItems(String email, Pageable pageable);

    Page<ItemResponse> searchItemsByDescription(String description, String email, Pageable pageable);


}
