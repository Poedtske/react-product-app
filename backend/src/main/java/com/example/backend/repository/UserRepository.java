package com.example.backend.repository;

import com.example.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);
}
