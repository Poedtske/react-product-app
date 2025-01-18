package com.example.backend.repository;

import com.example.backend.model.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Long> {
    Optional<RestaurantOrder> findById(Long orderId);
}
