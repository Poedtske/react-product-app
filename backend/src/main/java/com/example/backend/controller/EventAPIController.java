package com.example.backend.controller;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.enums.EventType;
import com.example.backend.model.Event;
import com.example.backend.service.impl.EventServiceImpl;
import com.example.backend.service.impl.TableServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventAPIController {


    private final EventServiceImpl eventService;
    private final TableServiceImpl tableService;

    @GetMapping("/public/events")
    public Iterable<Event> getAllEvents(){
        return eventService.findAll();
    }

    @GetMapping("/public/events/{id}")
    public Event GetEvent(@PathVariable Long id){
        return eventService.findById(id);
    }

    @PostMapping("/admin/events")
    public ResponseEntity<EventDto> addEvent(@RequestBody EventDto eventDto ){
        return eventService.createEvent(eventDto);
    }

    @GetMapping("/public/eventTypes")
    public List<String> getEventTypes() {
        return Arrays.stream(EventType.values())
                .map(Enum::name)
                .toList();
    }

    @DeleteMapping("/admin/events/{id}")
    public ResponseEntity deleteEvent(@PathVariable Long id){
        try{
            eventService.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e);
        }

    }

}
