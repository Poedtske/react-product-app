package com.example.backend.service.impl;

import com.example.backend.controller.AuthenticationResponse;
import com.example.backend.dto.EventDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.enums.EventType;
import com.example.backend.enums.Role;
import com.example.backend.model.Event;
import com.example.backend.model.EventDate;
import com.example.backend.model.Tafel;
import com.example.backend.model.User;
import com.example.backend.repository.DateDao;
import com.example.backend.repository.EventDao;
import com.example.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventRepository;

    @Autowired
    private DateDao dateDao;

    @Autowired
    private TableServiceImpl tableService;


    @Override
    public Event save(Event event) {
        if(event.getSpondId()==null){
            event.CreateLayout();
        }

        return eventRepository.save(event);
    }

    @Override
    public Event updateById(Long id, Event updatedEvent) {
        Event e = this.findById(id);
        e.setTitle(updatedEvent.getTitle());
        e.setPoster(updatedEvent.getPoster());
        e.setType(updatedEvent.getType());
        e.setLocation(updatedEvent.getLocation());
        e.setDescription(updatedEvent.getDescription());
        e.setSeatsPerTable(updatedEvent.getSeatsPerTable());


        return this.save(e);
    }

    @Override
    public Iterable<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findDistinctById(id);
    }

    @Override
    public Event findById(String id) {
        return eventRepository.findDistinctBySpondId(id);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.delete(eventRepository.findDistinctById(id));
    }

    @Override
    public void deleteById(String id) {
        eventRepository.delete(eventRepository.findDistinctBySpondId(id));
    }

    @Override
    public void deleteEvent(Event e) {
        eventRepository.delete(e);
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  // For "date"
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // For "startTime" and "endTime"

    // Method to parse the date string (from the form)
    private Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();  // Log error
            return null;
        }
    }

    // Method to parse the time string and combine it with the date string
    private Date parseTime(String dateStr, String timeStr) {
        try {
            String dateTimeStr = dateStr + " " + timeStr; // Combine date and time
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateTimeStr);
        } catch (Exception e) {
            e.printStackTrace();  // Log error
            return null;
        }
    }

    public ResponseEntity<EventDto> createEvent(EventDto eventDto) {
        try {
            // List to store EventDate objects
            List<EventDate> eventDates = new ArrayList<>();

            // event object is filled with the data gained from the form
            Event event = new Event(
                    eventDto.getTitle(),
                    eventDto.getLocation(),
                    eventDto.getDescription(),
                    EventType.valueOf(eventDto.getType()),
                    eventDto.getLayout(),
                    eventDto.getSeatsPerTable(),
                    eventDto.getTicketPrice()
            );
            eventRepository.save(event);

            // Iterate over the dates provided in the form and create EventDate objects
            for (Map<String, String> dateMap : eventDto.getDates()) {
                String dateStr = dateMap.get("date"); // Expected format: dd/MM/yyyy
                String startTimeStr = dateMap.get("startTime"); // Expected format: HH:mm
                String endTimeStr = dateMap.get("endTime"); // Expected format: HH:mm

                // Convert the strings to Date objects (we'll assume the helper methods parse correctly)
                Date date = parseDate(dateStr);
                Date startTime = parseTime(dateStr, startTimeStr);
                Date endTime = parseTime(dateStr, endTimeStr);

                // Create EventDate object and associate it with the Event
                EventDate eventDate = new EventDate(date, startTime, endTime, event);
                dateDao.save(eventDate);
                eventDates.add(eventDate);
            }

            // Set the list of EventDates in the Event object
            event.setDates(eventDates);

            tableService.CreateTables(event);

            // Save the event object along with its EventDates
            eventRepository.save(event);

            // Create the response DTO with the saved event data
            EventDto savedEventDto = new EventDto(
                    event.getId(),
                    event.getTitle(),
                    event.getLocation(),
                    event.getType().toString(),
                    event.getDescription(),
                    event.getSeatsPerTable(),
                    event.getLayout(),
                    eventDto.getDates(), // Send the dates back in the response
                    event.getDates(), // Include the saved EventDates in the response
                    event.getTicketPrice()
            );

            return ResponseEntity.ok(savedEventDto);

        } catch (Exception e) {
            // Handle the error by returning the EventDto with a bad request status
            e.printStackTrace();  // For logging the error
            return ResponseEntity.badRequest().body(eventDto);
        }
    }








}
