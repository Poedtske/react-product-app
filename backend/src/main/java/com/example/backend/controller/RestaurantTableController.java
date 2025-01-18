package com.example.backend.controller;

import com.example.backend.dto.RestaurantClientDto;
import com.example.backend.dto.RestaurantTableDto;
import com.example.backend.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/secure/restaurant/tables")
@RequiredArgsConstructor
public class RestaurantTableController {
    private final RestaurantTableService service;

    @GetMapping()
    public ResponseEntity getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTable(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @PostMapping()
    public ResponseEntity createTable() {
        return service.save();
    }

    @PutMapping()
    public ResponseEntity updateTable(@RequestBody RestaurantTableDto tableDto) {
        return service.updateById(tableDto.getId(), tableDto);
    }
}
