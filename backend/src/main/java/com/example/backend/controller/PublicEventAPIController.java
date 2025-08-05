package com.example.backend.controller;

import com.example.backend.dto.SpondEventDto;
import com.example.backend.model.Event;
import com.example.backend.repository.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/public/events")
public class PublicEventAPIController {

    private EventDao myEventDao;

    @Autowired
    public PublicEventAPIController(EventDao myEventDao){
        this.myEventDao=myEventDao;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public Iterable<Event> getAllEvents(){
        return myEventDao.findAll();
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

    @GetMapping("/{id}")
    public Event GetEvent(@PathVariable Long id){
        return myEventDao.findDistinctById(id);
    }




}
