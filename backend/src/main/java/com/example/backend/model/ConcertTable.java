package com.example.backend.model;

import jakarta.persistence.*;

import java.util.HashSet;

@Entity
@Table(name="ConcertTables")
public class ConcertTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany()
    @JoinTable(
            name = "Events_Products",
            joinColumns =@JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private HashSet<ConcertTicket> tickets;

    private int seats;
    private int availableSeats;
}
