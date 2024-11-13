package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;

@Entity
@Table(name="Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd:MM:yyyy")
    private Date startDate;

    @Nullable
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd:MM:yyyy")
    private Date endDate;

    private String name;

    @OneToMany(mappedBy = "event")
    private HashSet<com.example.backend.model.Date>dates;

    private String location;

    @Nullable
    @OneToMany(mappedBy = "event")
    private HashSet<ConcertTicket> tickets;

    @Nullable
    @OneToMany(mappedBy = "event")
    private HashSet<Tafel> tables;

    @Nullable
    @ManyToMany(mappedBy = "events")
    private HashSet<Product> products;

    @Nullable
    private String description;

    public Event() {
    }

    public Event(Date startDate, Date endDate, String name, HashSet<com.example.backend.model.Date>dates, String location, HashSet<ConcertTicket> concertTickets, HashSet<Tafel> tables, HashSet<Product> products, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.dates = dates;
        this.location = location;
        this.tickets = concertTickets;
        this.tables = tables;
        this.products = products;
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public HashSet<com.example.backend.model.Date> getDates() {
        return dates;
    }

    public HashSet<ConcertTicket> getTickets() {
        return tickets;
    }

    public HashSet<Tafel> getTables() {
        return tables;
    }

    public HashSet<Product> getProducts() {
        return products;
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
        this.products.add(p);
        return products.stream().filter(product -> product.getId()== product.getId()).findFirst().orElse(null);
    }

    public Tafel AddTable(Tafel t){
        this.tables.add(t);
        return t;
    }

    public Tafel RemoveTable(Tafel t){
        this.tables.remove(t);
        return t;
    }

    public Tafel GetTable(Tafel t){
        this.tables.add(t);
        return tables.stream().filter(table -> table.getId()==t.getId()).findFirst().orElse(null);
    }

    public ConcertTicket AddTicket(ConcertTicket t) {
        this.tickets.add(t);
        return t;
    }

    public ConcertTicket DeleteTicket(ConcertTicket t) {
        this.tickets.remove(t);
        return t;
    }

    public ConcertTicket GetTicket(ConcertTicket t){
        return this.tickets.stream().filter(ticket->ticket.getId()==t.getId()).findFirst().orElse(null);
    }
}
