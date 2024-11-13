package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;

@Entity
@Table(name = "ConcertTickets")
public class ConcertTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "tickets")
    private HashSet<Tafel> tables;

    private String owner;

    private BigDecimal price;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "event_id",referencedColumnName = "id")
    private Event event;

    private Boolean paid;

    @Nullable
    @OneToMany(mappedBy = "ticket")
    private HashSet<Date> days;

    public ConcertTicket() {
    }

    public ConcertTicket(HashSet<Tafel> tables, String owner, BigDecimal price, Event event, Boolean paid, HashSet<Date> days) {
        this.tables = tables;
        this.owner = owner;
        this.price = price;
        this.event = event;
        this.paid = paid;
        this.days = days;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Long getId() {
        return id;
    }

    public HashSet<Tafel> getTables() {
        return tables;
    }

    public HashSet<Date> getDays() {
        return days;
    }
}
