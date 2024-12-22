package com.example.backend.service.impl;

import com.example.backend.dto.EventDto;
import com.example.backend.enums.EventType;
import com.example.backend.exceptions.AppException;
import com.example.backend.model.Event;
import com.example.backend.model.EventDate;
import com.example.backend.repository.DateDao;
import com.example.backend.repository.EventDao;
import com.example.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    private static String IMG_DIR=System.getProperty("user.dir") + "/backend/src/main/resources/images/events";
    private static Path IMGS_PATH= Paths.get(IMG_DIR);

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


    public ResponseEntity updateById(Long id, Event updatedEvent, MultipartFile imageFile) {
        Event e = this.findById(id);
        e.setTitle(updatedEvent.getTitle());
        e.setPoster(updatedEvent.getPoster());
        e.setType(updatedEvent.getType());
        e.setLocation(updatedEvent.getLocation());
        e.setDescription(updatedEvent.getDescription());
        e.setSeatsPerTable(updatedEvent.getSeatsPerTable());


        return ResponseEntity.ok(0);
    }


    public ResponseEntity updateById(Long id, Event updatedEvent) {

        try{
            Event e=eventRepository.findById(id).orElseThrow(()->new AppException("Event not found",HttpStatus.NOT_FOUND));
            e.setTitle(updatedEvent.getTitle());
            e.setType(updatedEvent.getType());
            e.setLocation(updatedEvent.getLocation());
            e.setDescription(updatedEvent.getDescription());
            e.setSeatsPerTable(updatedEvent.getSeatsPerTable());

            e.setLayout(updatedEvent.getLayout());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }




        return ResponseEntity.ok(0);
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

    public ResponseEntity createEvent(EventDto eventDto, MultipartFile imageFile) {
        try {
            // Input format received
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Output format required
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Parse and reformat dates
            Date startTime = inputFormat.parse(eventDto.getStartTime());
            Date endTime = inputFormat.parse(eventDto.getEndTime());

            String formattedStartTime = outputFormat.format(startTime);
            String formattedEndTime = outputFormat.format(endTime);

            // Create Event
            Event event = new Event(
                    eventDto.getTitle(),
                    eventDto.getLocation(),
                    eventDto.getDescription(),
                    EventType.valueOf(eventDto.getType()),
                    startTime,
                    endTime,
                    eventDto.getLayout(),
                    eventDto.getSeatsPerTable(),
                    eventDto.getTicketPrice()
            );
            eventRepository.save(event);

            tableService.CreateTables(event);

            // Handle image
            String newFilename = event.getTitle() + "_" + event.getId() + "." +
                    StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
            Path newFilePath = IMGS_PATH.resolve(newFilename);
            Files.write(newFilePath, imageFile.getBytes());
            event.setImg(newFilename);
            eventRepository.save(event);

            // Response
            EventDto savedEventDto = new EventDto(
                    event.getId(),
                    event.getTitle(),
                    event.getLocation(),
                    event.getType().toString(),
                    event.getDescription(),
                    formattedStartTime,  // Send back in ISO format
                    formattedEndTime,    // Send back in ISO format
                    event.getSeatsPerTable(),
                    event.getLayout(),
                    event.getImg(),
                    event.getTicketPrice()
            );

            return new ResponseEntity<>(savedEventDto, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(eventDto);
        }
    }








}
