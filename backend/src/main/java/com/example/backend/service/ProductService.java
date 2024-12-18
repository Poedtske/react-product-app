package com.example.backend.service;

import com.example.backend.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Product save(Product product);

    Product updateById(Long id, Product product, MultipartFile imageFile) throws IOException;

    List<Product> findAll();

    Product findById(Long id);

    ResponseEntity deleteById(Long id);
}
