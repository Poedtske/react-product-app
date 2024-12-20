package com.example.backend.service;

import com.example.backend.dto.ProductDto;
import com.example.backend.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Product save(Product product);

    ResponseEntity updateById(Long id, ProductDto productDto, MultipartFile imageFile);

    List<Product> findAll();

    Product findById(Long id);

    ResponseEntity deleteById(Long id);
}
