package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String spondId;

    @Nullable
    private String poster;

    @Nullable
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd:MM:yyyy")
    private Date startDate;

    @Nullable
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd:MM:yyyy")
    private Date endDate;

    private String title;

    @Nullable
    @OneToMany(mappedBy = "event")
    private Set<com.example.backend.model.Date>dates;

    private String location;

    @Nullable
    @OneToMany(mappedBy = "event")
    private Set<ConcertTicket> tickets;

    @Nullable
    @OneToMany(mappedBy = "event")
    private Set<Tafel> tables;

    @Nullable
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private java.util.Date startTime;

    @Nullable
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private java.util.Date endTime;

    @Nullable
    @ManyToMany(mappedBy = "events")
    private Set<Product> products;

    @Nullable
    private String description;

    public Event() {
    }

    // Full constructor for initializing an Event with all fields
    public Event(Date startDate, Date endDate, String title, Set<com.example.backend.model.Date> dates,
                 String location, Set<ConcertTicket> tickets, Set<Tafel> tables, Set<Product> products, String description) {
        this.spondId = null;
        this.startTime = null;
        this.endTime = null;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.dates = dates;
        this.location = location;
        this.tickets = tickets;
        this.tables = tables;
        this.products = products;
        this.description = description;
    }

    // Constructor for creating an event from "Spond" data
    public Event(String spondId, java.util.Date startTime, java.util.Date endTime, String title, String location, String description) {
        this.spondId = spondId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startDate = null;
        this.endDate = null;
        this.dates = null;
        this.tickets = null;
        this.tables = null;
        this.products = null;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getSpondId() {
        return spondId;
    }

    public void setSpondId(String spondId) {
        this.spondId = spondId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
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

    public Set<com.example.backend.model.Date> getDates() {
        return dates;
    }

    public Set<ConcertTicket> getTickets() {
        return tickets;
    }

    public Set<Tafel> getTables() {
        return tables;
    }

    public Set<Product> getProducts() {
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

    public void SpondUpdate(Event e){
        this.startTime = e.startTime;
        this.endTime = e.endTime;
        this.title = e.title;
        this.location = e.location;
        if(e.description!=null){
            this.description = e.description;
        }
    }
}
