package com.example.backend.service.impl;

import com.example.backend.dto.TicketDto;
import com.example.backend.mapper.TicketMapper;
import com.example.backend.model.Ticket;
import com.example.backend.repository.TicketRepository;
import com.example.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketDto save(TicketDto ticketDto) {
        // Convert DTO to Entity
        Ticket ticket = ticketMapper.toTicket(ticketDto);

        // Save the Entity
        Ticket savedTicket = ticketRepository.save(ticket);

        // Convert the saved Entity back to DTO
        return ticketMapper.toTicketDto(savedTicket);
    }


    @Override
    public TicketDto updateById(Long id, TicketDto ticketDto) {
        // Fetch the existing Ticket entity
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        // Map TicketDto to Ticket entity to update fields
        Ticket updatedTicket = ticketMapper.toTicket(ticketDto);

        // Update fields from DTO while keeping relationships intact
        existingTicket.setPrice(updatedTicket.getPrice());
        existingTicket.setPaid(updatedTicket.getPaid());
        existingTicket.setTable(updatedTicket.getTable());
        existingTicket.setOwner(updatedTicket.getOwner());
        existingTicket.setEvent(updatedTicket.getEvent());

        // Save and map back to DTO
        Ticket savedTicket = ticketRepository.save(existingTicket);

        return ticketMapper.toTicketDto(savedTicket);
    }

    @Override
    public Iterable<TicketDto> findAll() {
        // Fetch all entities and map them to DTOs
        return StreamSupport.stream(ticketRepository.findAll().spliterator(), false)
                .map(ticketMapper::toTicketDto) // Map each Ticket to TicketDto
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto findById(Long id) {
        // Fetch the entity and map it to DTO
        return ticketRepository.findById(id)
                .map(ticketMapper::toTicketDto) // Map to TicketDto
                .orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

}
