package com.example.backend.controller;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.TicketDto;
import com.example.backend.model.Ticket;
import com.example.backend.service.TicketService;
import com.example.backend.service.impl.TicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketServiceImpl ticketService;

    @GetMapping("/api/admin/events/{eventId}/tickets")
    public ResponseEntity<Iterable<TicketDto>> getTickets(@PathVariable("eventId") Long eventId){
        return ResponseEntity.ok(ticketService.findAll());
    }

    @GetMapping("/api/admin/tickets/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") Long id){
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @PostMapping("/api/secure/tickets")
    public ResponseEntity createTicket(@RequestBody TicketDto ticketDto ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the logged-in username
        return ticketService.createTicket(username,ticketDto);
    }

}
