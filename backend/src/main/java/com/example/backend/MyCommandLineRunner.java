package com.example.backend;

import com.example.backend.model.Product;
import com.example.backend.repository.RestaurantTableRepository;
import com.example.backend.service.ProductService;
import com.example.backend.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyCommandLineRunner implements CommandLineRunner {


    private final RestaurantTableService service;
    private final RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public MyCommandLineRunner(RestaurantTableService service, RestaurantTableRepository restaurantTableRepository) {
        this.service = service;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if the table already has 31 or more records
        if (restaurantTableRepository.findAll().stream().count() >= 31) {
            System.out.println("Seeding skipped: 31 or more products already exist in the database.");
            return;
        }

        // Seed data
        for (int i = 1; i <= 31; i++) {
            service.save();
        }

        System.out.println("Seeding completed: 31 products added to the database.");
    }
}
