package com.example.backend.controller;

import com.example.backend.model.Event;
import com.example.backend.service.impl.EventServiceImpl;
import com.example.backend.service.impl.TableServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventViewsController {
    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private TableServiceImpl tableService;

    @GetMapping()
    public String getEvents(ModelMap myModelMap){
        myModelMap.addAttribute("events", eventService.findAll());
        return "events/index";
    }

    @GetMapping("/{id}")
    public String showEvent(ModelMap myModelMap, @PathVariable Long id){
        Event e= eventService.findById(id);
        if(e==null){
            return "redirect:/events";
        }else{
            myModelMap.addAttribute("event",e);
            return "events/show";
        }
    }

    @ModelAttribute("newEvent")
    public Event newEvent(){
        return new Event();
    }

    @GetMapping("/create")
    public String createEvent(@ModelAttribute("newEvent") Event newEvent){
        return "events/create";
    }

    @PostMapping()
    public String addEvent(@ModelAttribute("newEvent") @Valid Event newEvent, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "events/create";
        }
        eventService.save(newEvent);
        tableService.CreateTables(newEvent);

        return "redirect:/events";
    }

    @PutMapping("/{id}")
    public String editEvent(@PathVariable Long id,
                            @ModelAttribute("newEvent") @Valid Event newEvent,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "events/create"; // Or an edit-specific view like "events/edit"
        }

        eventService.updateById(id,newEvent);
        return "redirect:/index";
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id){
        eventService.deleteById(id);
        return "redirect:/events";
    }



}
