package com.example.backend.config;

import com.example.backend.enums.Role;
import com.example.backend.model.Account;
import com.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            Account admin = new Account();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setFirstName("Admin");
            admin.setLastName("Account");
            admin.setRoles(Set.of(Role.ADMIN));
            userRepository.save(admin);

            Account account = new Account();
            account.setEmail("account@example.com");
            account.setPassword(passwordEncoder.encode("userpass"));
            account.setFirstName("Regular");
            account.setLastName("Account");
            account.setRoles(Set.of(Role.USER));
            userRepository.save(account);
        };
    }
}

