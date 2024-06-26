package com.invoiceapi.services;

import com.invoiceapi.models.response.AuthenticationResponse;
import com.invoiceapi.models.dtos.LoginUserDto;
import com.invoiceapi.models.dtos.RegisterUserDto;
import com.invoiceapi.exceptions.EntityAlreadyExistsException;
import com.invoiceapi.models.enums.Role;
import com.invoiceapi.models.entities.User;
import com.invoiceapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthenticationResponse register(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setFirstName(registerUserDto.firstName());
        user.setLastName(registerUserDto.lastName());
        user.setEmail(registerUserDto.email());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(registerUserDto.password()));
        if (userRepository.existsByEmail(registerUserDto.email())) {
            throw new EntityAlreadyExistsException("User with this email already exists");
        }
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, jwtService.getExpirationTime());
    }

    public AuthenticationResponse authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.email(),
                        loginUserDto.password()
                )
        );
        var user = userRepository.findByEmail(loginUserDto.email())
                .orElseThrow(() -> new EntityAlreadyExistsException("User not found with email: " + loginUserDto.email()));

        if (!passwordEncoder.matches(loginUserDto.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, jwtService.getExpirationTime());
    }
}
