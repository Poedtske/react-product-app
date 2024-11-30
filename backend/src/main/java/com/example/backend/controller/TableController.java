package com.example.backend.controller;

import com.example.backend.model.Event;
import com.example.backend.model.Tafel;
import com.example.backend.service.impl.EventServiceImpl;
import com.example.backend.service.impl.TableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableServiceImpl tableService;

    @Autowired
    private EventServiceImpl eventService;

    @PostMapping()
    public String addTable(@RequestBody Tafel t) {
        // Check if an event with the same spondId already exists
        if ((t.getEvent()) != null) {
            tableService.save(t);
            Event e=t.getEvent();
            e.AddTable(t);
            return "a new table was added to event "+e.getTitle();
        } else {
            return "Event was not defined";
        }
    }

    @GetMapping("/{id}")
    public Tafel GetEvent(@PathVariable Long id){
        return tableService.findById(id);
    }

}
