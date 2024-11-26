package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.HashSet;

@Entity
@Table(name="Tables")
public class Tafel {

    //@EmbeddedId TableId id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @ManyToMany
    @JoinTable(
            name = "Tickets_Tables",
            joinColumns =@JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    private HashSet<ConcertTicket> tickets;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Event event;

    @Nullable
    private int seats;

    @Nullable
    private int availableSeats;

    public Tafel() {
    }

    public Tafel(Event event, HashSet<ConcertTicket> tickets, int seats, int availableSeats) {
        this.tickets = tickets;
        this.seats = seats;
        this.availableSeats = availableSeats;
        this.event=event;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Long getId(){
        return id;
    }

    public HashSet<ConcertTicket> getTickets() {
        return tickets;
    }

    public Event getEvent() {
        return event;
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
