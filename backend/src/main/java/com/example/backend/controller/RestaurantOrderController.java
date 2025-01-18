package com.example.backend.controller;

import com.example.backend.dto.RestaurantOrderDto;
import com.example.backend.model.Order;
import com.example.backend.model.RestaurantOrder;
import com.example.backend.service.RestaurantOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/secure/restaurant/orders")
@RequiredArgsConstructor
public class RestaurantOrderController {
    private final RestaurantOrderService orderService;

    @GetMapping()
    public ResponseEntity getOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrder(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable Long id) {
        return orderService.delete(id);
    }

    @PostMapping()
    public ResponseEntity createOrder(@RequestBody RestaurantOrderDto order) {
        return orderService.save(order);
    }

    @PutMapping()
    public ResponseEntity updateOrder(@RequestBody RestaurantOrderDto order) {
        return orderService.updateById(order.getId(), order);
    }

}
