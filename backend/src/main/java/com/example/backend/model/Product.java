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

    //if it has to be displayed in the webshop
    private Boolean online;

    //if it's a child's portion
    private Boolean childPortion;

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
    private Set<Order> orders;

    public Product() {
    }

    public Product(String name, BigDecimal price, String img, Boolean available, Boolean online, Boolean childPortion, HashSet<Event> events, Category category, HashSet<Order> orders) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.available = available;
        this.online = online;
        this.childPortion = childPortion;
        this.events = events;
        this.category = category;
        this.orders = orders;
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

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getChildPortion() {
        return childPortion;
    }

    public void setChildPortion(Boolean childPortion) {
        this.childPortion = childPortion;
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

    public Set<Order> getOrders() {
        return orders;
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
