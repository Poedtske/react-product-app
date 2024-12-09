package com.example.backend.dto;

import com.example.backend.enums.EventType;
import com.example.backend.model.EventDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
    private int seatsPerTable;
    private String layout;
    private List<Map<String,String>> Dates;
    private List<EventDate> eventDates;

}
