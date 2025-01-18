package com.example.backend.controller;

import com.example.backend.dto.CategoryDto;
import com.example.backend.model.Category;
import com.example.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/secure/restaurant/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategory(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping()
    public ResponseEntity createCategory(@RequestBody CategoryDto category) {
        return categoryService.save(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        return categoryService.deleteById(id);
    }

    @PutMapping
    public ResponseEntity updateCategory(@RequestBody CategoryDto category) {
        return categoryService.updateById(category.getId(), category);
    }
}
