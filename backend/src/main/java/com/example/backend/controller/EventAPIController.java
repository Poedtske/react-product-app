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

    @DeleteMapping("/spond/{spondId}")
    public String DeleteSpondEvent(@PathVariable String spondId, @RequestBody Event e){
        myEventDao.delete(myEventDao.findDistinctBySpondId(spondId));
        return "Spond event has been deleted";
    }

    @PutMapping("/spond")
    public String UpdateSpondEvent(@RequestBody Event e){
        Event oldEvent=myEventDao.findDistinctBySpondId(e.getSpondId());
        oldEvent.SpondUpdate(e);
        myEventDao.save(oldEvent);
        return "Spond event has been updated";
    }

    @GetMapping("/{id}")
    public Event GetEvent(@PathVariable Long id){
        return myEventDao.findDistinctById(id);
    }

    @GetMapping("/spond")
    public Iterable<SpondEventDto> getAllSpondEvents() {
        Iterable<Event> allEvents = myEventDao.findAll();
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
