package com.example.backend.controller;

import com.example.backend.dto.TableForm;
import com.example.backend.model.Event;
import com.example.backend.model.Tafel;
import com.example.backend.service.impl.EventServiceImpl;
import com.example.backend.service.impl.TableServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/tables")
public class TableViewsController {
    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private TableServiceImpl tableService;



    @GetMapping("/{id}")
    public String showTable(ModelMap myModelMap,
                            @PathVariable Long id,
                            HttpServletRequest request) {
        Tafel t = tableService.findById(id);
        if (t == null) {
            // Redirect to the previous page using the "Referer" header
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/events");
        } else {
            myModelMap.addAttribute("table", t);
            return "tables/show";
        }
    }


    @GetMapping("/edit/{id}")
    public String seeEditTable(@PathVariable Long id, ModelMap model,HttpServletRequest request) {
        Tafel t = tableService.findById(id); // Fetch the event using its ID
        if (t == null) {
            // Redirect to the previous page using the "Referer" header
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/events");
        } else {
            model.addAttribute("table",t);
            return "tables/edit"; // Return the edit view
        }

    }


    @ModelAttribute("newTable")
    public Tafel newTable(){
        return new Tafel();
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("newTable", new TableForm());
        model.addAttribute("events", eventService.findAll()); // Fetch all events
        return "tables/create";
    }


    @PostMapping()
    public String createTables(@ModelAttribute TableForm form, RedirectAttributes redirectAttributes) {
        int aantalTafels = form.getAantalTafels();
        Event selectedEvent = eventService.findById(form.getEventId());

        if (selectedEvent == null) {
            redirectAttributes.addFlashAttribute("error", "Selected event does not exist.");
            return "redirect:/tables/create";
        }

        for (int i = 0; i < aantalTafels; i++) {
            Tafel table = new Tafel(selectedEvent, form.getSeats(),form.getWidth(),form.getHeight());
            tableService.save(table);
        }
        return "redirect:/events";
    }


    @PutMapping("/{id}")
    public String editEvent(@PathVariable Long id,
                            @ModelAttribute("newEvent") @Valid Event newEvent,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "events/create"; // Or an edit-specific view like "events/edit"
        }
        eventService.updateById(id, newEvent);
        return "redirect:/index";
    }


    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) throws IOException {
        eventService.deleteById(id);
        return "redirect:/events";
    }



}
