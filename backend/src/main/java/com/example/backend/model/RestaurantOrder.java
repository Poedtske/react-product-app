package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_orders")
@Getter
@Setter
public class RestaurantOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id",referencedColumnName = "id")
    private RestaurantClient client;


    @Nullable
    @ManyToMany(mappedBy = "orders")
    private List<RestaurantProduct> productList = new ArrayList<>();

    public RestaurantOrder() {

    }

    public RestaurantOrder(RestaurantClient client, List<RestaurantProduct> productList) {
        this.client = client;
        this.productList = productList;
    }

    public void addProduct(RestaurantProduct product) {
        productList.add(product);
    }

    public void removeProduct(RestaurantProduct product) {
        productList.remove(product);
    }

    public RestaurantProduct getProduct(Long productId) {
        return productList.stream().filter(product -> product.getId().equals(productId)).findFirst().orElse(null);
    }
}
