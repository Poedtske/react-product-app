package com.example.backend.controller;

import com.example.backend.dto.RestaurantProductDto;
import com.example.backend.model.Product;
import com.example.backend.model.RestaurantProduct;
import com.example.backend.service.RestaurantProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/secure/restaurant/products")
@RequiredArgsConstructor
public class RestaurantProductController {
    private final RestaurantProductService service;

    @GetMapping()
    public ResponseEntity getProducts() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getProduct(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping()
    public ResponseEntity createProduct(@RequestBody RestaurantProductDto productDto) {
        return service.save(productDto);
    }

    @PutMapping()
    public ResponseEntity updateProduct(@RequestBody RestaurantProductDto productDto) {
        return service.updateById(productDto.getId(), productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
