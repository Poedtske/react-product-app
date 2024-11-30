package com.example.backend.service.impl;

import com.example.backend.enums.Role;
import com.example.backend.model.Account;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        Account account = new Account();
        account.setFirstName(userDto.getFirstName());
        account.setLastName(userDto.getLastName());
        account.setEmail(userDto.getEmail());
        account.setPassword(passwordEncoder.encode(userDto.getPassword()));
        account.setRoles(Set.of(Role.USER));

        userRepository.save(account);
    }
}

