package com.example.backend.controller;

import com.example.backend.dto.RestaurantClientDto;
import com.example.backend.dto.RestaurantOrderDto;
import com.example.backend.service.RestaurantClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/secure/restaurant/clients")
@RequiredArgsConstructor
public class RestaurantClientController {
    private final RestaurantClientService service;

    @GetMapping()
    public ResponseEntity getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @PostMapping()
    public ResponseEntity createClient(@RequestBody RestaurantClientDto clientDto) {
        return service.save(clientDto);
    }

    @PutMapping()
    public ResponseEntity updateClient(@RequestBody RestaurantClientDto clientDto) {
        return service.updateById(clientDto.getId(), clientDto);
    }

    @PostMapping("/{clientId}/orders/{orderId}")
    public ResponseEntity addOrder(@PathVariable Long clientId, @PathVariable Long orderId) {
        return service.addOrder(clientId, orderId);
    }

    @DeleteMapping("/{clientId}/orders/{orderId}")
    public ResponseEntity removeOrder(@PathVariable Long clientId, @PathVariable Long orderId) {
        return service.deleteOrder(clientId, orderId);
    }

    @PutMapping("/{clientId}/tables/{tableId}")
    public ResponseEntity switchTables(@PathVariable Long clientId, @PathVariable Long tableId) {
        return service.changeTable(clientId,tableId);

    }
}
