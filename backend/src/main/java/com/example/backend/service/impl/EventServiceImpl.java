package com.example.backend.service.impl;

import com.example.backend.dto.EventDto;
import com.example.backend.exceptions.AppException;
import com.example.backend.model.Event;
import com.example.backend.model.Invoice;
import com.example.backend.model.Ticket;
import com.example.backend.repository.DateDao;
import com.example.backend.repository.EventDao;
import com.example.backend.repository.InvoiceRepository;
import com.example.backend.repository.TicketRepository;
import com.example.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    private static String IMG_DIR=System.getProperty("user.dir") + "/backend/src/main/resources/images/events";
    private static Path IMGS_PATH= Paths.get(IMG_DIR);

    @Autowired
    private EventDao eventRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

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
    public ResponseEntity updateById(Long id, Event event) {
        return null;
    }


    public ResponseEntity updateById(Long id, EventDto updatedEvent, MultipartFile imageFile) {
        try{
            //finds event, if it's not found it throws an exception
            Event e=eventRepository.findById(id).orElseThrow(()->new AppException("Event not found",HttpStatus.NOT_FOUND));

            updatedEvent.CreateLayout();

            e.setTitle(updatedEvent.getTitle());
            e.setType(updatedEvent.getType());
            e.setLocation(updatedEvent.getLocation());
            e.setDescription(updatedEvent.getDescription());
            e.setStartTime(dateParsingForEvent(updatedEvent.getStartTime()));
            e.setEndTime(dateParsingForEvent(updatedEvent.getEndTime()));

            noTicketsUpdate(updatedEvent,e);

            if(e.getImg()!=null){
                //delete the current img
                if (!Files.exists(IMGS_PATH)) {
                    return ResponseEntity.internalServerError().body(null);
                }

                Path filePath = IMGS_PATH.resolve(e.getImg());
                if (!Files.exists(filePath)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                Files.deleteIfExists(filePath);
            }

            //save the new img

            // Generate the new filename and construct the file path
            String newFilename = e.getTitle() + "_" + e.getId() + "." +
                    StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
            Path newFilePath = IMGS_PATH.resolve(newFilename);

            // Write the file with the new name
            Files.write(newFilePath, imageFile.getBytes());

            // Update the product with the relative path to the image
            //String relativePath = "images/" + newFilename;
            e.setImg(newFilename);

            eventRepository.save(e);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Update without img
    public ResponseEntity updateById(Long id, EventDto updatedEvent) {

        try{
            //finds event, if it's not found it throws an exception
            Event e=eventRepository.findById(id).orElseThrow(()->new AppException("Event not found",HttpStatus.NOT_FOUND));

            updatedEvent.CreateLayout();

            e.setTitle(updatedEvent.getTitle());
            e.setType(updatedEvent.getType());
            e.setLocation(updatedEvent.getLocation());
            e.setDescription(updatedEvent.getDescription());
            e.setStartTime(dateParsingForEvent(updatedEvent.getStartTime()));
            e.setEndTime(dateParsingForEvent(updatedEvent.getEndTime()));

            noTicketsUpdate(updatedEvent,e);

            eventRepository.save(e);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }





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

    //Event
    @Override
    public ResponseEntity deleteById(Long id) throws IOException {
        Event e=eventRepository.findById(id).orElseThrow(()->new AppException("Event not found",HttpStatus.NOT_FOUND));
        return handleDelete(e);
    }

    private ResponseEntity handleDelete(Event e){
        try{
            boolean hasUnclosedPaidInvoice = e.getTickets().stream()
                    .anyMatch(ticket -> ticket.getInvoice().getPaid() && !ticket.getInvoice().getClosed());



            if (hasUnclosedPaidInvoice){
                return ResponseEntity.internalServerError().body("Cannot be deleted, there are invoices that have been paid and are still open");
            }

            e.getTickets().forEach(ticket -> {
                Invoice i=ticket.getInvoice();
                if(i.getPaid()==false){

                    i.removeTicket(ticket);
                    invoiceRepository.save(i);
                } else if (i.getPaid()==true && i.getClosed()==true) {
                    invoiceRepository.delete(i);
                }
            });
            //p.getInvoices().removeIf(invoice -> invoice.getPaid()==false);
            //p.getInvoices().removeIf(invoice -> invoice.getPaid()==true && invoice.getClosed()==true);

            eventRepository.save(e);

            if (!Files.exists(IMGS_PATH)) {
                return ResponseEntity.internalServerError().body(null);
            }

            Path filePath = IMGS_PATH.resolve(e.getImg());
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            eventRepository.deleteById(e.getId());
            Files.deleteIfExists(filePath);
            return ResponseEntity.ok().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
    //SpondEvent
    @Override
    public ResponseEntity deleteById(String id) {
        Event e= eventRepository.findDistinctBySpondId(id);
        return handleDelete(e);
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
            // Create Event
            Event event = new Event(
                    eventDto.getTitle(),
                    eventDto.getLocation(),
                    eventDto.getDescription(),
                    eventDto.getType(),
                    dateParsingForEvent(eventDto.getStartTime()),
                    dateParsingForEvent(eventDto.getEndTime()),
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
                    event.getType(),
                    event.getDescription(),
                    dateParsingForEventDto(event.getStartTime()),  // Send back in ISO format
                    dateParsingForEventDto(event.getEndTime()),    // Send back in ISO format
                    event.getSeatsPerTable(),
                    event.getImg(),
                    event.getLayout(),
                    event.getTicketPrice(),
                    event.getRijen(),
                    event.getKolommen()
            );

            return new ResponseEntity<>(savedEventDto, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(eventDto);
        }
    }

    public ResponseEntity<Resource> getImg(long id) {
        try {
            // Retrieve the product
            Event e = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

            // Construct the file path

            if (!Files.exists(IMGS_PATH)) {
                return ResponseEntity.internalServerError().body(null);
            }

            Path filePath = IMGS_PATH.resolve(e.getImg());
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Load the file as a Resource
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable");
            }

            // Determine the content type of the image
            String contentType = Files.probeContentType(filePath);
            if (StringUtils.isEmpty(contentType)) {
                contentType = "application/octet-stream"; // Fallback content type
            }

            // Return the image file as a response
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType)) // Set the correct content type for your images
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }


    }

    private Date dateParsingForEvent(String date) throws ParseException {
        // Input format received
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return inputFormat.parse(date);
    }

    private String dateParsingForEventDto(Date date) throws ParseException {
        // Input format received
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFormat.format(date);
    }

    private void noTicketsUpdate(EventDto updatedEvent, Event e){
        //boolean for checking if the ticketPrice, layout or seatsPerTable have been changed

        if (e.getTicketPrice()==null){
            e.setTicketPrice(BigDecimal.ZERO);
        }

        Boolean noTickets = !(e.getTicketPrice().compareTo(updatedEvent.getTicketPrice()) != 0 ||
                !e.getLayout().equals(updatedEvent.getLayout()) ||
                e.getSeatsPerTable() != updatedEvent.getSeatsPerTable());


        //use the noTickets boolean to decide if there should be certain changes
        if(!noTickets){

            Set<Ticket> ticketList=e.getTickets();
            ticketList.stream().filter(ticket -> ticket.getInvoice().getPaid()==true);
            //these changes cannot happen when there already have been tickets sold
            if(!ticketList.isEmpty()){
                throw new AppException("You cannot change the update the layout or price when this event has sold tickets",HttpStatus.BAD_REQUEST);
            }

            //updates the ticketPrice for the event and the all the tickets
            if(e.getTicketPrice()!=updatedEvent.getTicketPrice()){
                e.setTicketPrice(updatedEvent.getTicketPrice());
                e.getTickets().stream().forEach(ticket -> {
                    ticket.setPrice(e.getTicketPrice());
                    ticketRepository.save(ticket);
                });
            }

            if(e.getSeatsPerTable()==null){
                e.setSeatsPerTable(0);
            }

            //updates the seatsPerTable for the event and the all the tables
            if(e.getSeatsPerTable()!=updatedEvent.getSeatsPerTable()){

                e.setSeatsPerTable(updatedEvent.getSeatsPerTable());

                e.getTables().stream().forEach(tafel -> {
                    tafel.setSeats(e.getSeatsPerTable());
                    tableService.save(tafel);
                });
            }

            if(e.getLayout()==null){
                e.setLayout("0x0");
                e.CreateLayout();
            }
            //updates the amount of tables in the event
            if(!e.getLayout().equals(updatedEvent.getLayout())){

                if((e.getRijen()*e.getKolommen())>(updatedEvent.getRijen()*updatedEvent.getKolommen())){

                    tableService.RemoveTables(e,(e.getRijen()*e.getKolommen()-updatedEvent.getRijen()*updatedEvent.getKolommen()));
                    e.setLayout(updatedEvent.getLayout());
                    e.CreateLayout();
                }else{

                    tableService.CreateTables(e,(e.getRijen()*e.getKolommen()-updatedEvent.getRijen()*updatedEvent.getKolommen()));
                    e.setLayout(updatedEvent.getLayout());
                    e.CreateLayout();
                }
            }
        }
    }
}
