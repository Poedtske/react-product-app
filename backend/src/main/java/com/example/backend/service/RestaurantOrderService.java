package com.example.backend.service;

import com.example.backend.dto.RestaurantOrderDto;
import com.example.backend.model.RestaurantOrder;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestaurantOrderService {
    ResponseEntity findById(Long id);
    ResponseEntity findAll();
    ResponseEntity save(RestaurantOrderDto restaurantOrderDto);
    ResponseEntity delete(Long id);
    ResponseEntity updateById(Long id, RestaurantOrderDto restaurantOrderDto);

}
