package com.example.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
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
