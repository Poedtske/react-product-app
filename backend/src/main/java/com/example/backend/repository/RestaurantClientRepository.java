package com.example.backend.repository;

import com.example.backend.model.RestaurantClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RestaurantClientRepository extends JpaRepository<RestaurantClient, Long> {
    Optional<RestaurantClient> findById(Long clientId);
}
