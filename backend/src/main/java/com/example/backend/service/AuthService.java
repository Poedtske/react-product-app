package com.example.backend.service;

import com.example.backend.config.JwtService;
import com.example.backend.controller.AuthenticationResponse;
import com.example.backend.dto.CredentialsDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.enums.Role;
import com.example.backend.model.Invoice;
import com.example.backend.model.User;
import com.example.backend.repository.InvoiceRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final InvoiceRepository invoiceRepository;

    public AuthenticationResponse register(SignUpDto signUpDto){
        var user= User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .email(signUpDto.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();
        userRepository.save(user);

        Invoice i=new Invoice(user);
        invoiceRepository.save(i);

        user.addInvoice(i);
        userRepository.save(user);
        Map<String, Object> role=new HashMap<>();
        role.put("role",user.getRole());
        var jwtToken=jwtService.generateToken(role,user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(CredentialsDto credentialsDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentialsDto.getEmail(),
                        credentialsDto.getPassword()));
        var user= userRepository.findByEmail(credentialsDto.getEmail()).orElseThrow();
        Map<String, Object> role=new HashMap<>();
        role.put("role",user.getAuthorities());
        var jwtToken=jwtService.generateToken(role,user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
