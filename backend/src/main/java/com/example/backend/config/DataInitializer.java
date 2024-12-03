package com.example.backend.config;

import com.example.backend.enums.Role;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
/*
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
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRoles(Set.of(Role.ADMIN));
            userRepository.save(admin);

            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("userpass"));
            user.setFirstName("Regular");
            user.setLastName("User");
            user.setRoles(Set.of(Role.USER));
            userRepository.save(user);
        };
    }
}
*/
