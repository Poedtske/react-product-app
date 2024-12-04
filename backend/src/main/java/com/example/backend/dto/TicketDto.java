package com.example.backend.dto;

import com.example.backend.model.Event;
import com.example.backend.model.Tafel;
import com.example.backend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TicketDto {
    private Long id;
    private Tafel table;
    private User owner;
    private BigDecimal price;
    private Event event;
    private Boolean paid;
}
