package com.example.backend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Categories")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String img;

    @Nullable
    @OneToMany(mappedBy = "category")
    private Set<RestaurantProduct> products;

    public Category() {
    }

    public Category(String name, String img) {
        this.name = name;
        this.img = img;
        this.products = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RestaurantProduct AddProduct(RestaurantProduct p){
        products.add(p);
        return p;
    }

    public RestaurantProduct RemoveProduct(RestaurantProduct p){
        products.remove(p);
        return p;
    }

    public RestaurantProduct GetProduct(RestaurantProduct p){
        return products.stream().filter(product -> product.getId()==p.getId()).findFirst().orElse(null);
    }

}
