package com.invoiceapi.controllers;

import com.invoiceapi.models.dtos.ItemDto;
import com.invoiceapi.models.response.InvoiceResponse;
import com.invoiceapi.models.response.ItemResponse;
import com.invoiceapi.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/list")
    public ResponseEntity<List<ItemResponse>> getAllListItems(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<ItemResponse> itemResponses = itemService.getAllListItems(email);
        return new ResponseEntity<>(itemResponses, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getAllItems(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int sizePerPage,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, sizePerPage, Sort.by(direction, sortBy));
        String email = userDetails.getUsername();
        Page<ItemResponse> itemResponses = itemService.getAllItems(email, pageable);
        return new ResponseEntity<>(itemResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemDto itemDto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ItemResponse itemResponse = itemService.createItem(itemDto, email);
        return new ResponseEntity<>(itemResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id,
                                                   @RequestBody ItemDto itemDto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ItemResponse itemResponse = itemService.updateItem(id, itemDto, email);
        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        itemService.deleteItem(id, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ItemResponse itemResponse = itemService.getItemById(id, email);
        return new ResponseEntity<>(itemResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemResponse>> searchItemsByDescription(
            @RequestParam String description,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int sizePerPage) {

        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = userDetails.getUsername();
        Pageable pageable = PageRequest.of(page, sizePerPage);
        Page<ItemResponse> items = itemService.searchItemsByDescription(description, email, pageable);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

}
