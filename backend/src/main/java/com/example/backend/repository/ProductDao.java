package com.example.backend.repository;

import com.example.backend.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao  extends CrudRepository<Product,Long> {
}
