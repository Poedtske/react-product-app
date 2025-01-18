package com.example.backend.service.impl;

import com.example.backend.dto.RestaurantClientDto;
import com.example.backend.dto.RestaurantOrderDto;
import com.example.backend.dto.RestaurantProductDto;
import com.example.backend.model.RestaurantClient;
import com.example.backend.model.RestaurantOrder;
import com.example.backend.model.RestaurantProduct;
import com.example.backend.repository.RestaurantClientRepository;
import com.example.backend.repository.RestaurantOrderRepository;
import com.example.backend.repository.RestaurantProductRepository;
import com.example.backend.service.RestaurantOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantOrderServiceImpl implements RestaurantOrderService {
    private final RestaurantOrderRepository orderRepository;
    private final RestaurantProductRepository productRepository;
    private final RestaurantClientRepository clientRepository;

    @Override
    public ResponseEntity findById(Long id) {
        try{
            RestaurantOrder order= orderRepository.findById(id).orElseThrow(Exception::new);

            return ResponseEntity.ok(new RestaurantOrderDto(
                    order.getId(),
                    new RestaurantClientDto(order.getClient().getId()),
                    order.getProductList().stream()
                            .map(product->
                                    new RestaurantProductDto(
                                            product.getId(),
                                            product.getName(),
                                            product.getPrice(),
                                            product.getAvailable(),
                                            product.getHidden()))
                            .collect(Collectors.toList())
            ));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity findAll() {
        try{
            List<RestaurantOrder> orders= orderRepository.findAll();
            return ResponseEntity.ok(orders.stream().map(order->
                    new RestaurantOrderDto(
                            order.getId()
                    )));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }    }

    @Override
    public ResponseEntity save(RestaurantOrderDto restaurantOrderDto) {
        try{
            RestaurantOrder order= new RestaurantOrder(
                    clientRepository.findById(restaurantOrderDto.getClient().getId()).orElseThrow(Exception::new),
                    restaurantOrderDto.getProducts().stream().map(product->
                            productRepository.findById(product.getId()).orElseThrow()).collect(Collectors.toList())
            );
            orderRepository.save(order);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        try{
            RestaurantOrder order= orderRepository.findById(id).orElseThrow(Exception::new);
            orderRepository.delete(order);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity updateById(Long id, RestaurantOrderDto restaurantOrderDto) {
        try{
            RestaurantOrder order= orderRepository.findById(id).orElseThrow(Exception::new);
            order.setClient(clientRepository.findById(restaurantOrderDto.getClient().getId()).orElseThrow(Exception::new));
            order.setProductList(restaurantOrderDto.getProducts().stream().
                    map(product->productRepository.findById(product.getId()).orElseThrow())
                    .collect(Collectors.toList())
            );
            orderRepository.save(order);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
