package com.example.backend.service;

import com.example.backend.dto.TicketDto;
import com.example.backend.model.Product;
import com.example.backend.model.Ticket;

import java.util.List;

public interface TicketService {
    TicketDto save(TicketDto ticket);

    TicketDto updateById(Long id, TicketDto ticket);

    Iterable<TicketDto> findAll();

    TicketDto findById(Long id);

    void deleteById(Long id);
}
