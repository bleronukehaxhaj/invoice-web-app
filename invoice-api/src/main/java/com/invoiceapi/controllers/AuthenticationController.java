package com.invoiceapi.controllers;

import com.invoiceapi.models.entities.User;
import com.invoiceapi.models.response.AuthenticationResponse;
import com.invoiceapi.models.dtos.LoginUserDto;
import com.invoiceapi.models.dtos.RegisterUserDto;
import com.invoiceapi.models.response.UserResponse;
import com.invoiceapi.services.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(authenticationService.register(registerUserDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginUserDto));
    }

}
