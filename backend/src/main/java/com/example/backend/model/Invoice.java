package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.HashSet;

@Entity
@Table(name = "Invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "invoice")
    private HashSet<Order> orders;

    @Nullable
    private String description;

    private Boolean paid;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_id",referencedColumnName = "id")
    private Tafel table;

    /*@JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;*/

    public Invoice() {
    }

    public Invoice(HashSet<Order> orders, String description, Boolean paid, Tafel table) {
        this.orders = orders;
        this.description = description;
        this.paid = paid;
        this.table = table;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Tafel getTable() {
        return table;
    }

    public void setTable(Tafel table) {
        this.table = table;
    }

    public Long getId() {
        return id;
    }

    public HashSet<Order> getOrders() {
        return orders;
    }
}
