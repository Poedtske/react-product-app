package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant_table")
@Getter
@Setter
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "table")
    private Set<RestaurantClient> clients= new HashSet<RestaurantClient>();


    public RestaurantTable() {

    }

    public void addClient(RestaurantClient client) {
        this.clients.add(client);
    }

    public void removeClient(RestaurantClient client) {
        this.clients.remove(client);
    }

    public RestaurantClient getClient(Long id) {
        return this.clients.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }
}
