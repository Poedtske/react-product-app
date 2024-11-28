package com.example.backend.controller;

import com.example.backend.model.Event;
import com.example.backend.service.impl.EventServiceImpl;
import jakarta.validation.Valid;
import com.example.backend.repository.EventDao;
import com.example.backend.dto.SpondEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/secure/events")
public class EventAPIController {

    @Autowired
    private EventServiceImpl service;

    @PostMapping()
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
    @PostMapping("/batch")
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

    @DeleteMapping("/spond")
    public String DeleteSpondEvent(@RequestBody Event e){
        service.deleteEvent(service.findById(e.getSpondId()));
        return "Spond event has been deleted";
    }

    @PutMapping("/spond")
    public String UpdateSpondEvent(@RequestBody Event e){
        Event oldEvent=service.findById(e.getSpondId());
        oldEvent.SpondUpdate(e);
        service.save(oldEvent);
        return "Spond event has been updated";
    }


}
