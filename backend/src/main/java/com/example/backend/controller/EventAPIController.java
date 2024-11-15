package com.example.backend.controller;

import com.example.backend.model.Event;
import jakarta.validation.Valid;
import com.example.backend.repository.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventAPIController {

    private EventDao myEventDao;

    @Autowired
    public EventAPIController(EventDao myEventDao){
        this.myEventDao=myEventDao;
    }

    @GetMapping
    public Iterable<Event> getAllEvents(){
        return myEventDao.findAll();
    }

    @PostMapping()
    public String addSpondEvent(@RequestBody Event e) {
        // Check if an event with the same spondId already exists
        if (myEventDao.findDistinctBySpondId(e.getSpondId()) != null) {
            return "Event with spondId " + e.getSpondId() + " already exists";
        } else {
            myEventDao.save(e);
            return "Event has been added";
        }
    }


    // New POST method for handling a batch of events
    @PostMapping("/batch")
    public String addMultipleEvents(@RequestBody List<Event> events) {
        for (Event event : events) {
            if (myEventDao.findDistinctBySpondId(event.getSpondId()) != null) {
                return "Event with spondId " + event.getSpondId() + " already exists";
            } else {
                myEventDao.save(event);
            }
        }
        return "All events have been added successfully";
    }

}
