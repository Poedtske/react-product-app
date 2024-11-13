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

    /*@Nullable
    @ManyToMany
    @JoinTable(
            name = "Tables_Tickets",
            joinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "eventId"),
                    @JoinColumn(name = "table_id", referencedColumnName = "tableId")
            },
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private HashSet<ConcertTicket> tickets;*/

    @Nullable
    @ManyToMany
    @JoinTable(
            name = "Tickets_Tables",
            joinColumns =@JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    private HashSet<ConcertTicket> tickets;

    @Nullable
    @OneToMany(mappedBy = "table")
    private HashSet<Invoice> invoices;


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

    public Tafel(Event event, HashSet<ConcertTicket> tickets, HashSet<Invoice> invoices, int seats, int availableSeats) {
        this.tickets = tickets;
        this.invoices = invoices;
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

    /*public TableId getId() {
        return id;
    }*/

    public Long getId(){
        return id;
    }

    public HashSet<ConcertTicket> getTickets() {
        return tickets;
    }

    public HashSet<Invoice> getInvoices() {
        return invoices;
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

    public Invoice AddInvoice(Invoice i) {
        this.invoices.add(i);
        return i;
    }

    public Invoice DeleteInvoice(Invoice i) {
        this.invoices.remove(i);
        return i;
    }

    public Invoice GetInvoice(Invoice i){
        return this.invoices.stream().filter(invoice->invoice.getId()==i.getId()).findFirst().orElse(null);
    }


}
