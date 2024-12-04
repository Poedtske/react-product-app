package com.example.backend.repository;

import com.example.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
