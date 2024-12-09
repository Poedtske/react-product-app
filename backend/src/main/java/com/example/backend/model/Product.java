package com.example.backend.model;

import com.example.backend.enums.Category;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private String img;

    //if it's available
    private Boolean available=true;

    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Nullable
    @ManyToMany(mappedBy = "products")
    private Set<Invoice> invoices =new HashSet<>();

    @Nullable
    @ManyToMany(mappedBy = "products")
    private List<User> users= new ArrayList<>();

    public Product() {
    }

    public Product(String name, BigDecimal price, String img, Boolean available, Category category, Set<Invoice> invoices) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.available = available;
        this.category = category;
        this.invoices = invoices;
        this.category=category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id); // Compare by id if it's not null
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0; // Use id hash code, fallback to 0 if id is null
    }

}
