package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.HashSet;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @OneToMany(mappedBy = "Products")
    private HashMap<Product,Integer> products;*/

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "invoice_id",referencedColumnName = "id")
    private Invoice invoice;

    @Nullable
    private String description;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_id",referencedColumnName = "id")
    private Tafel tafel;

    @ManyToMany()
    @JoinTable(
            name = "Orders_Products",
            joinColumns =@JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private HashSet<Product> products;

    /*@JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;*/

    public Order() {
    }

    public Order(Invoice invoice, String description, Tafel tafel) {
        this.invoice = invoice;
        this.description = description;
        this.tafel = tafel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Tafel getTafel() {
        return tafel;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setTafel(Tafel tafel) {
        this.tafel = tafel;
    }

    public void UpdateFields(Order o, Order update){
        o.setTafel(update.getTafel());
        o.setDescription(update.getDescription());
        o.setInvoice(update.getInvoice());
    }

    public Product AddProduct(Product p){
        this.products.add(p);
        return p;
    }

    public Product RemoveProduct(Product p){
        this.products.remove(p);
        return p;
    }

    public Product GetProduct(Product p){
        return this.products.stream().filter(product -> product.getId()==p.getId()).findFirst().orElse(null);
    }

}
