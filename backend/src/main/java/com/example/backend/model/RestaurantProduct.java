package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_product")
@Getter
@Setter
public class RestaurantProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Double price;
    private Boolean available;
    private Boolean hidden;
    private String img;

    @NotNull
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @Nullable
    @ManyToMany()
    @JoinTable(
            name = "restaurant_orders_products",
            joinColumns =@JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<RestaurantOrder> orders=new ArrayList<RestaurantOrder>();


    public RestaurantProduct() {

    }

    public RestaurantProduct(String name, Double price, Boolean available, Boolean hidden, Category category, String img) {
        this.name = name;
        this.price = price;
        this.available = available;
        this.hidden = hidden;
        this.category = category;
        this.img = img;
    }

    public void addOrder(RestaurantOrder order) {
        orders.add(order);
    }

    public void removeOrder(RestaurantOrder order) {
        orders.remove(order);
    }

    public RestaurantOrder findOrderById(Long id) {
        return orders.stream().filter(order -> order.getId().equals(id)).findFirst().orElse(null);
    }
}
