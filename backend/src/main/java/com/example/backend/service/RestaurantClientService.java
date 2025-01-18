package com.example.backend.service;

import com.example.backend.dto.RestaurantClientDto;
import com.example.backend.dto.RestaurantOrderDto;
import com.example.backend.dto.RestaurantTableDto;
import com.example.backend.model.RestaurantClient;
import com.example.backend.model.RestaurantOrder;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RestaurantClientService {

    ResponseEntity save(RestaurantClientDto clientDto);
    ResponseEntity findById(Long id);
    ResponseEntity findAll();
    ResponseEntity updateById(Long id, RestaurantClientDto clientDto);
    ResponseEntity deleteById(Long id);
    ResponseEntity addOrder(Long id, Long orderId);
    ResponseEntity deleteOrder(Long id, Long orderId);
    ResponseEntity changeTable(Long clientId,Long previousTableId, Long newTableId);

}
