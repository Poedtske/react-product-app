package com.example.backend.service.impl;

import com.example.backend.dto.RestaurantClientDto;
import com.example.backend.dto.RestaurantTableDto;
import com.example.backend.model.RestaurantTable;
import com.example.backend.repository.RestaurantTableRepository;
import com.example.backend.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;
    @Override
    public ResponseEntity save(RestaurantTableDto tableDto) {
        try{
            RestaurantTable table = restaurantTableRepository.findById(tableDto.getId()).orElseThrow(Exception::new);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findById(Long id) {
        try{
            RestaurantTable table = restaurantTableRepository.findById(id).orElseThrow(Exception::new);

            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findAll() {
        try{
            List<RestaurantTable> tables = restaurantTableRepository.findAll();


            return ResponseEntity.ok(tables.stream()
                    .map(table->
                        new RestaurantTableDto(
                                table.getId(),
                                table.getClients().stream()
                                        .map(client->
                                                new RestaurantClientDto(
                                                        client.getId()
                                                )
                                        ).collect(Collectors.toSet())
                        )
                    ).collect(Collectors.toList())
            );
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity updateById(Long id, RestaurantTableDto tableDto) {
        return null;
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        return null;
    }
}
