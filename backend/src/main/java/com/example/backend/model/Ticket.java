package com.example.backend.model;

import com.example.backend.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "Tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "table_id", referencedColumnName = "id", insertable = true, updatable = true)
    private Tafel table;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = true, updatable = false, nullable = false)
    private User owner;

    private BigDecimal price;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "event_id",referencedColumnName = "id")
    private Event event;

        private Boolean paid;

    public Ticket() {
    }

    public Ticket(Tafel table, User owner, BigDecimal price, Event event, Boolean paid) {
        this.table = table;
        this.owner = owner;
        this.price = price;
        this.event = event;
        this.paid = paid;
    }

    public void setTable(Tafel table) {
        this.table = table;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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


    public String getName() {
        return "Ticket "+getId()+" from event: "+getEvent().getTitle();
    }

    public Tafel getTable() {
        return table;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id != null && id.equals(ticket.id); // Compare based on id
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0; // Hash based on id
    }
}
