package com.invoiceapi.services;

import com.invoiceapi.mappers.ItemMapper;
import com.invoiceapi.models.dtos.ItemDto;
import com.invoiceapi.models.entities.Invoice;
import com.invoiceapi.models.entities.Item;
import com.invoiceapi.models.entities.User;
import com.invoiceapi.models.response.InvoiceResponse;
import com.invoiceapi.models.response.ItemResponse;
import com.invoiceapi.repositories.ItemRepository;
import com.invoiceapi.repositories.UserRepository;
import com.invoiceapi.services.interfaces.ItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponse createItem(ItemDto itemDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Item item = itemMapper.toEntityFromDto(itemDto);
        item.setUser(user);
        var savedItem = itemRepository.save(item);
        return itemMapper.toResponseFromEntity(savedItem);
    }

    @Override
    public ItemResponse updateItem(Long id, ItemDto itemDto, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        item.setDescription(itemDto.description());
        item.setUnit(itemDto.unit());
        item.setRate(itemDto.rate());
        var updatedItem = itemRepository.save(item);
        return itemMapper.toResponseFromEntity(updatedItem);
    }

    @Override
    public void deleteItem(Long id, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.delete(item);

    }

    @Override
    public ItemResponse getItemById(Long id, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        var item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        return itemMapper.toResponseFromEntity(item);
    }

    @Override
    public List<ItemResponse> getAllListItems(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Item> items = itemRepository.findAllByUser(user);
        return items.stream()
                .map(itemMapper::toResponseFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ItemResponse> getAllItems(String email, Pageable pageable) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Page<Item> itemsPage = itemRepository.findAllByUser(user, pageable);
        return itemsPage.map(itemMapper::toResponseFromEntity);
    }

    @Override
    public Page<ItemResponse> searchItemsByDescription(String description, String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Page<Item> items = itemRepository.findByDescriptionContainingIgnoreCase(description, pageable);

        List<ItemResponse> filteredItems = items.getContent().stream()
                .filter(item -> item.getUser().equals(user))
                .map(itemMapper::toResponseFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(filteredItems, pageable, items.getTotalElements());
    }
}
