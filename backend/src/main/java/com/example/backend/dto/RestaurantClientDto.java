package com.example.backend.dto;

import com.example.backend.model.RestaurantTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantClientDto {
    private Long id;
    private RestaurantTableDto table;
    private Set<RestaurantOrderDto> orders;
    private Boolean paid;

    public RestaurantClientDto(Long id, RestaurantTableDto table, Set<RestaurantOrderDto> orders, Boolean paid) {
        this.id = id;
        this.table = table;
        this.orders = orders;
        this.paid = paid;
    }

    public RestaurantClientDto(Long id, RestaurantTableDto table, Boolean paid) {
        this.id = id;
        this.table = table;
        this.paid = paid;
    }

    public RestaurantClientDto(Long id) {
        this.id = id;
    }

    public RestaurantClientDto(RestaurantTableDto table) {
        this.table = table;
    }

    public RestaurantClientDto(Long id, Boolean paid) {
        this.id = id;
        this.paid = paid;
    }
}
