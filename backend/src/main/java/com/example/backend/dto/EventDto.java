package com.example.backend.dto;

import com.example.backend.enums.EventType;
import com.example.backend.model.EventDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder
@Data
public class EventDto {
    private Long id;
    private String title;
    private String location;
    private String type;
    private String description;
    private String startTime;
    private String endTime;
    private int seatsPerTable;
    private String img;
    private String layout;
    private BigDecimal ticketPrice;

}
