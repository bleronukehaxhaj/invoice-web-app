package com.invoiceapi.controllers;

import com.invoiceapi.models.dtos.BusinessDto;
import com.invoiceapi.models.response.BusinessResponse;
import com.invoiceapi.services.interfaces.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/businesses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<BusinessResponse> createBusiness(@RequestBody BusinessDto businessDto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        BusinessResponse createdBusiness = businessService.createBusiness(businessDto, email);
        return new ResponseEntity<>(createdBusiness, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponse> updateBusiness(@PathVariable Long id, @RequestBody BusinessDto businessDto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        BusinessResponse updatedBusiness = businessService.updateBusiness(id, businessDto, email);
        return new ResponseEntity<>(updatedBusiness, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        businessService.deleteBusiness(id, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponse> getBusinessById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        BusinessResponse business = businessService.getBusinessById(id, email);
        return new ResponseEntity<>(business, HttpStatus.OK);
    }

    @GetMapping("/my-business")
    public ResponseEntity<BusinessResponse> getMyBusiness(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        BusinessResponse businessResponse = businessService.getBusinessByUser(email);
        return ResponseEntity.ok(businessResponse);
    }
}
