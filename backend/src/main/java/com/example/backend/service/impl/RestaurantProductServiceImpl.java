package com.example.backend.service.impl;

import com.example.backend.dto.RestaurantProductDto;
import com.example.backend.model.RestaurantProduct;
import com.example.backend.repository.RestaurantProductRepository;
import com.example.backend.service.RestaurantProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantProductServiceImpl implements RestaurantProductService {
    private final RestaurantProductRepository restaurantProductRepository;

    @Override
    public ResponseEntity save(RestaurantProductDto productDto) {
        try{
            RestaurantProduct product =restaurantProductRepository.findById(productDto.getId()).orElseThrow();
            restaurantProductRepository.save(product);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findById(Long id) {
        try{
            RestaurantProduct product =restaurantProductRepository.findById(id).orElseThrow();
            return ResponseEntity.ok(new RestaurantProductDto(product.getId(), product.getName(), product.getPrice(), product.getAvailable(), product.getHidden()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findAll() {
        try{
            List<RestaurantProduct> products = restaurantProductRepository.findAll();
            return ResponseEntity.ok(products.stream().map(product-> new RestaurantProductDto(product.getId(), product.getName(), product.getPrice(), product.getAvailable(), product.getHidden())));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity updateById(Long id, RestaurantProductDto productDto) {
        try{
            RestaurantProduct product =restaurantProductRepository.findById(id).orElseThrow();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setAvailable(productDto.getAvailable());
            product.setHidden(productDto.getHidden());
            restaurantProductRepository.save(product);
            return ResponseEntity.ok().build();

        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        try{
            RestaurantProduct product =restaurantProductRepository.findById(id).orElseThrow();
            restaurantProductRepository.delete(product);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
