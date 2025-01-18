package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class RestaurantClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_id",referencedColumnName = "id")
    private RestaurantTable table;

    @OneToMany(mappedBy = "client")
    private Set<RestaurantOrder> orders = new HashSet<RestaurantOrder>();

    private Boolean paid;

    public RestaurantClient(RestaurantTable table, Boolean paid) {
        this.table = table;
        this.paid = paid;
    }

    public RestaurantClient() {

    }

    public void addOrder(RestaurantOrder order) {
        orders.add(order);
    }

    public void removeOrder(RestaurantOrder order) {
        orders.remove(order);
    }

    public RestaurantOrder findOrderById(Long id) {
        return this.orders.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
    }
}
