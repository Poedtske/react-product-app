package com.example.backend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.HashSet;

@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Nullable
    @OneToMany(mappedBy = "category")
    private HashSet<Product> products;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
        this.products = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public HashSet<Product> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product AddProduct(Product p){
        products.add(p);
        return p;
    }

    public Product RemoveProduct(Product p){
        products.remove(p);
        return p;
    }

    public Product GetProduct(Product p){
        return products.stream().filter(product -> product.getId()==p.getId()).findFirst().orElse(null);
    }

}
