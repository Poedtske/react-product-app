package com.example.backend.dto;

import com.example.backend.model.RestaurantProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoryDto {

    private Long id;
    private String name;
    private String img;
    private Set<RestaurantProductDto> products;

    public CategoryDto(Long id, String name, String img) {
        this.id = id;
        this.name = name;
        this.products = null;
        this.img = img;
    }

    public CategoryDto() {
    }

    public CategoryDto(Long id) {
        this.id = id;
    }

    public CategoryDto(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public CategoryDto(Long id, String name, Set<RestaurantProduct> products, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.products = products.stream().map(product->new RestaurantProductDto(
                product.getId(),
                product.getName(),
                product.getImg(),
                product.getPrice(),
                product.getAvailable(),
                product.getHidden()
                )).collect(Collectors.toSet());
    }
}
