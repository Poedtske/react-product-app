package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class RestaurantTableDto {
    private Long id;
    private Set<RestaurantClientDto> clients;

    public RestaurantTableDto(Long id, Set<RestaurantClientDto> clients) {
        this.id = id;
        this.clients = clients;
    }

    public RestaurantTableDto(Long id) {
        this.id = id;
    }
}
