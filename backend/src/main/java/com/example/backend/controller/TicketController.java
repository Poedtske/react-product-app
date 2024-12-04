package com.example.backend.controller;

import com.example.backend.dto.TicketDto;
import com.example.backend.model.Ticket;
import com.example.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/api/admin/events/{eventId}/tickets")
    public ResponseEntity<Iterable<TicketDto>> getTickets(@PathVariable("eventId") Long eventId){
        return ResponseEntity.ok(ticketService.findAll());
    }

    @GetMapping("/api/admin/tickets/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") Long id){
        return ResponseEntity.ok(ticketService.findById(id));
    }

}
