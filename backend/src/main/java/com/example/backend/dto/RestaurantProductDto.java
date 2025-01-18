package com.example.backend.dto;

import com.example.backend.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantProductDto {
    private Long id;
    private String name;
    private Double price;
    private Boolean available;
    private Boolean hidden;
    private CategoryDto category;
    private List<RestaurantOrderDto> orders;
    private String img;

    public RestaurantProductDto(Long id, String img, String name, Double price, Boolean available, Boolean hidden, Category category) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
        this.available = available;
        this.hidden = hidden;
        this.category = new CategoryDto(category.getId(),category.getName(),category.getImg());
        this.orders=null;
    }

    public RestaurantProductDto(String name,String img, Double price, Boolean available, Boolean hidden, CategoryDto category) {
        this.id = null;
        this.name = name;
        this.img = img;
        this.price = price;
        this.available = available;
        this.hidden = hidden;
        this.category = category;
        this.orders=null;
    }

    public RestaurantProductDto(Long id, String Img, String name, Double price, Boolean available, Boolean hidden) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
        this.hidden = hidden;
        this.category = null;
        this.orders=null;
    }
}
