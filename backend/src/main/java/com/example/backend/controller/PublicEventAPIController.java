package com.example.backend.controller;

import com.example.backend.dto.SpondEventDto;
import com.example.backend.model.Event;
import com.example.backend.repository.EventDao;
import com.example.backend.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/public/events")
public class PublicEventAPIController {

    @Autowired
    private EventServiceImpl service;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public Iterable<Event> getAllEvents(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Event GetEvent(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping("/spond")
    public Iterable<SpondEventDto> getAllSpondEvents() {
        Iterable<Event> allEvents = service.findAll();
        List<SpondEventDto> eventDtos = new ArrayList<>();

        for (Event event : allEvents) {
            if (event.getSpondId() != null) {  // Only include events with a non-null spondId
                SpondEventDto eventDto;

                // Check if the description is null
                if (event.getDescription() == null) {
                    eventDto = new SpondEventDto(
                            event.getSpondId(),
                            event.getStartTime().toString(),  // Convert to String if necessary
                            event.getEndTime().toString(),    // Convert to String if necessary
                            event.getTitle(),
                            event.getLocation()
                    );
                } else {
                    eventDto = new SpondEventDto(
                            event.getSpondId(),
                            event.getStartTime().toString(),  // Convert to String if necessary
                            event.getEndTime().toString(),    // Convert to String if necessary
                            event.getTitle(),
                            event.getLocation(),
                            event.getDescription()  // Only add description if it's not null
                    );
                }

                eventDtos.add(eventDto);  // Add the DTO to the list
            }
        }


        return eventDtos;
    }


}
