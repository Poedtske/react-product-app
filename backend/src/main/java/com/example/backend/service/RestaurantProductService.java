package com.example.backend.service;

import com.example.backend.dto.RestaurantProductDto;
import com.example.backend.model.RestaurantClient;
import com.example.backend.model.RestaurantProduct;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestaurantProductService {
    ResponseEntity save(RestaurantProductDto product);
    ResponseEntity findById(Long id);
    ResponseEntity findAll();
    ResponseEntity updateById(Long id, RestaurantProductDto product);
    ResponseEntity deleteById(Long id);
}
