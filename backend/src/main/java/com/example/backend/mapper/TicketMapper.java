package com.example.backend.mapper;

import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.TicketDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.Ticket;
import com.example.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel ="spring")
public interface TicketMapper {


    Ticket toTicket(TicketDto ticketDto);

    TicketDto toTicketDto(Ticket ticket);
}
