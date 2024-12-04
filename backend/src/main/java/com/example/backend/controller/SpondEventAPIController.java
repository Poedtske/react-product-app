package com.example.backend.controller;

import com.example.backend.dto.SpondEventDto;
import com.example.backend.model.Event;
import com.example.backend.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class SpondEventAPIController {

    @Autowired
    private EventServiceImpl service;

    @PostMapping("/api/secure/events")
    public String addSpondEvent(@RequestBody Event e) {
        // Check if an event with the same spondId already exists
        if (service.findById(e.getSpondId()) != null) {
            return "Event with spondId " + e.getSpondId() + " already exists";
        } else {
            service.save(e);
            return "Event has been added";
        }
    }


    // New POST method for handling a batch of events
    @PostMapping("/api/secure/events/batch")
    public String addMultipleEvents(@RequestBody List<Event> events) {
        for (Event event : events) {
            if (service.findById(event.getSpondId()) != null) {
                return "Event with spondId " + event.getSpondId() + " already exists";
            } else {
                service.save(event);
            }
        }
        return "All events have been added successfully";
    }

    @DeleteMapping("/api/secure/events/spond")
    public String DeleteSpondEvent(@RequestBody Event e){
        service.deleteEvent(service.findById(e.getSpondId()));
        return "Spond event has been deleted";
    }

    @PutMapping("/api/secure/events/spond")
    public String UpdateSpondEvent(@RequestBody Event e){
        Event oldEvent=service.findById(e.getSpondId());
        oldEvent.SpondUpdate(e);
        service.save(oldEvent);
        return "Spond event has been updated";
    }

    @GetMapping("/api/public/events/spond")
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
