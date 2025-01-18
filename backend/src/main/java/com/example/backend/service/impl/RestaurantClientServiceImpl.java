package com.example.backend.service.impl;

import com.example.backend.dto.RestaurantClientDto;
import com.example.backend.dto.RestaurantOrderDto;
import com.example.backend.dto.RestaurantProductDto;
import com.example.backend.dto.RestaurantTableDto;
import com.example.backend.model.RestaurantClient;
import com.example.backend.model.RestaurantOrder;
import com.example.backend.model.RestaurantProduct;
import com.example.backend.model.RestaurantTable;
import com.example.backend.repository.RestaurantClientRepository;
import com.example.backend.repository.RestaurantOrderRepository;
import com.example.backend.repository.RestaurantProductRepository;
import com.example.backend.repository.RestaurantTableRepository;
import com.example.backend.service.RestaurantClientService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantClientServiceImpl implements RestaurantClientService {
    private final RestaurantClientRepository restaurantClientRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantOrderRepository restaurantOrderRepository;
    @Override
    public ResponseEntity save(RestaurantClientDto clientDto) {
        try{
            restaurantClientRepository.save(new RestaurantClient(restaurantTableRepository.findById(clientDto.getTable().getId()).orElseThrow(),clientDto.getPaid()));
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findById(Long id) {
        try{
            RestaurantClient client = restaurantClientRepository.findById(id).orElseThrow();
            return ResponseEntity.ok(
                    new RestaurantClientDto(
                            client.getId(),
                            new RestaurantTableDto(
                                    client.getTable().getId()),
                            client.getOrders().stream()
                                    .map(order->new RestaurantOrderDto(
                                            order.getId(),
                                            order.getProductList().stream()
                                                    .map(product->new RestaurantProductDto(
                                                            product.getId(),
                                                            product.getName(),
                                                            product.getImg(),
                                                            product.getPrice(),
                                                            product.getAvailable(),
                                                            product.getHidden())).
                                                    collect(Collectors.toList()))).
                                    collect(Collectors.toSet()),
                            client.getPaid()
                    )
            );
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findAll() {
        try{
            List<RestaurantClient> clients = restaurantClientRepository.findAll();
            return ResponseEntity.ok(clients.stream().map(client->new RestaurantClientDto(client.getId(), new RestaurantTableDto(client.getTable().getId()), client.getPaid())));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity updateById(Long id, RestaurantClientDto clientDto) {
        try{
            RestaurantClient client = restaurantClientRepository.findById(id).orElseThrow();
            client.setPaid(clientDto.getPaid());
            restaurantClientRepository.save(client);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        try{
            RestaurantClient client = restaurantClientRepository.findById(id).orElseThrow();
            if(client.getPaid()){
                restaurantClientRepository.deleteById(id);
            }else{
                throw new RuntimeException("Client not found");
            }
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity addOrder(Long id, Long orderId) {
        try{
            RestaurantClient client = restaurantClientRepository.findById(id).orElseThrow();
            client.addOrder(restaurantOrderRepository.findById(orderId).orElseThrow());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity deleteOrder(Long id, Long orderId) {
        try{
            RestaurantClient client = restaurantClientRepository.findById(id).orElseThrow();
            client.removeOrder(restaurantOrderRepository.findById(orderId).orElseThrow());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity changeTable(Long clientId, Long newTableId) {
        try{
            RestaurantClient client = restaurantClientRepository.findById(clientId).orElseThrow();
            client.setTable(restaurantTableRepository.findById(newTableId).orElseThrow());
            restaurantClientRepository.save(client);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
