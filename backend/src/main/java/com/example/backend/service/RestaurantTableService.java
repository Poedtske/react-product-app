package com.example.backend.service;

import com.example.backend.dto.RestaurantTableDto;
import com.example.backend.model.RestaurantClient;
import com.example.backend.model.RestaurantTable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestaurantTableService {
    ResponseEntity save();
    ResponseEntity findById(Long id);
    ResponseEntity findAll();
    ResponseEntity updateById(Long id, RestaurantTableDto tableDto);
    ResponseEntity deleteById(Long id);
}
