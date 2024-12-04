package com.example.backend.controller;

import com.example.backend.model.Event;
import com.example.backend.service.impl.EventServiceImpl;
import com.example.backend.service.impl.TableServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventAPIController {


    private final EventServiceImpl eventService;
    private final TableServiceImpl tableService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/public/events")
    public Iterable<Event> getAllEvents(){
        return eventService.findAll();
    }

    @GetMapping("/public/events/{id}")
    public Event GetEvent(@PathVariable Long id){
        return eventService.findById(id);
    }

    @PostMapping("/admin/events")
    public Event addEvent(@ModelAttribute("newEvent") @Valid Event newEvent, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return null;
        }
        eventService.save(newEvent);
        tableService.CreateTables(newEvent);

        return eventService.findById(newEvent.getId());
    }


}
