package com.example.backend.service;

import com.example.backend.dto.CategoryDto;
import com.example.backend.dto.RestaurantProductDto;
import com.example.backend.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity save(CategoryDto category);
    ResponseEntity findById(Long id);
    ResponseEntity findAll();
    ResponseEntity updateById(Long id, CategoryDto category);
    ResponseEntity deleteById(Long id);
    ResponseEntity addProduct(RestaurantProductDto productDto, Long id);
    ResponseEntity removeProduct(RestaurantProductDto productDto,Long id);
}
