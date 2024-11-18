package com.example.backend.controller;

import com.example.backend.model.Event;
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
@RequestMapping("/api/private/events")
public class EventAPIController {

    private EventDao myEventDao;

    @Autowired
    public EventAPIController(EventDao myEventDao){
        this.myEventDao=myEventDao;
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

    @DeleteMapping("/spond")
    public String DeleteSpondEvent(@RequestBody Event e){
        myEventDao.delete(myEventDao.findDistinctBySpondId(e.getSpondId()));
        return "Spond event has been deleted";
    }

    @PutMapping("/spond")
    public String UpdateSpondEvent(@RequestBody Event e){
        Event oldEvent=myEventDao.findDistinctBySpondId(e.getSpondId());
        oldEvent.SpondUpdate(e);
        myEventDao.save(oldEvent);
        return "Spond event has been updated";
    }


}
