package com.example.backend.repository;

import com.example.backend.model.RestaurantProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantProductRepository extends JpaRepository<RestaurantProduct, Long> {
    Optional<RestaurantProduct> findById(Long productId);
}
