package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
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
    private Boolean available;

    private int quantity;

    @Nullable
    @ManyToMany()
    @JoinTable(
            name = "Events_Products",
            joinColumns =@JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Event> events;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @Nullable
    @ManyToMany(mappedBy = "products")
    private Set<Invoice> invoices;

    public Product() {
    }

    public Product(String name, BigDecimal price, String img, Boolean available, HashSet<Event> events, Category category, Set<Invoice> invoices) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.available = available;
        this.events = events;
        this.category = category;
        this.invoices = invoices;
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

    public Set<Event> getEvents() {
        return events;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public Event AddEvent(Event e){
        this.events.add(e);
        return e;
    }

    public Event RemoveEvent(Event e){
        this.events.remove(e);
        return e;
    }

    public Event GetEvent(Event e){
        return this.events.stream().filter(event -> event.getId()==e.getId()).findFirst().orElse(null);
    }
}
