package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestaurantOrderDto {
    private Long id;
    private RestaurantClientDto client;
    private List<RestaurantProductDto> products;

    public RestaurantOrderDto(Long id, RestaurantClientDto client, List<RestaurantProductDto> products) {
        this.id = id;
        this.client = client;
        this.products = products;
    }

    public RestaurantOrderDto(Long id, List<RestaurantProductDto> products) {
        this.id = id;
        this.products = products;
    }

    public RestaurantOrderDto(Long id) {
        this.id = id;
    }
}
