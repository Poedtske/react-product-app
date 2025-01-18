package com.example.backend.service.impl;

import com.example.backend.dto.CategoryDto;
import com.example.backend.dto.RestaurantProductDto;
import com.example.backend.model.Category;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.RestaurantProductRepository;
import com.example.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantProductRepository productRepository;

    @Override
    public ResponseEntity save(CategoryDto categoryDto) {
        try{
            Category category = new Category(categoryDto.getName(), categoryDto.getImg());
            categoryRepository.save(category);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findById(Long id) {
        try{
            Category category = categoryRepository.findById(id).orElseThrow();

            return ResponseEntity.ok(new CategoryDto(category.getId(),category.getName(),category.getProducts(),category.getImg()));
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findAll() {
        try{
            List<Category> categoryList = categoryRepository.findAll();
            List<CategoryDto> categoryDtoList = categoryList.stream().map(c->new CategoryDto(c.getId(),c.getName(),c.getImg())).toList();
            return ResponseEntity.ok(categoryDtoList);
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity updateById(Long id, CategoryDto categoryDto) {
        try{
            Category foundCategory = categoryRepository.findById(id).orElseThrow();
            foundCategory.setName(categoryDto.getName());
            foundCategory.setImg(categoryDto.getImg());
            categoryRepository.save(foundCategory);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        try{
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity addProduct(RestaurantProductDto productDto, Long id) {
         try {
             Category category= categoryRepository.findById(id).orElseThrow();
             category.AddProduct(productRepository.findById(productDto.getId()).orElseThrow());
             categoryRepository.save(category);
             return ResponseEntity.ok().build();
         }catch(Exception e){
             return ResponseEntity.internalServerError().build();
         }
    }

    @Override
    public ResponseEntity removeProduct(RestaurantProductDto productDto, Long id) {
        try {
            Category category= categoryRepository.findById(id).orElseThrow();
            category.RemoveProduct(productRepository.findById(productDto.getId()).orElseThrow());
            categoryRepository.save(category);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
