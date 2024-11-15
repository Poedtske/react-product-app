package com.example.backend;

import com.example.backend.model.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        Product product = new Product();
        product.setName("iPhone");
        product.setPrice(BigDecimal.valueOf(100D));
        product.setQuantity(99);
        productService.save(product);

        Product product2 = new Product();
        product2.setName("Blackberry");
        product2.setPrice(BigDecimal.valueOf(1100D));
        product2.setQuantity(55);
        productService.save(product2);
    }
}
