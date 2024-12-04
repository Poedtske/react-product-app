package com.example.backend.model;

import com.example.backend.enums.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.Date;

@Entity
@Table(name="Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String spondId;

    @Nullable
    private String poster;

    @NotBlank
    private String title;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private Set<Date> dates=new HashSet<>();

    @NotBlank
    private String location;

    @NotNull
    private EventType type=EventType.UNKNOWN;

    @Nullable
    @OneToMany(mappedBy = "event")
    private Set<Ticket> tickets=new HashSet<>();

    @Nullable
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tafel> tables=new ArrayList<>();
    //tafelNr= positie Tafel

    @Nullable
    private int rijen;
    @Nullable
    private int kolommen;
    @Nullable
    private Integer seatsPerTable;
    @Nullable
    private String layout;

    @Nullable
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private java.util.Date startTime;

    @Nullable
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
    private java.util.Date endTime;

    @Nullable
    private String description;

    public Event() {
    }

    // Full constructor for initializing an Event with all fields
    public Event(String title,
                 String location, String description, EventType type, String layout, int seatsPerTable) {
        this.spondId = null;
        this.startTime = null;
        this.endTime = null;
        this.title = title;
        this.dates = new HashSet<>();
        this.location = location;
        this.description = description;
        this.type = type;
        this.seatsPerTable = seatsPerTable;
        this.layout=layout;
    }


    // Constructor for creating an event from "Spond" data
    public Event(String spondId, java.util.Date startTime, java.util.Date endTime, String title, String location, String description) {
        this.spondId = spondId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.location = location;
        this.description = description;
        dates.add(startTime);
    }

    public int getRijen() {
        return rijen;
    }

    public int getKolommen() {
        return kolommen;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getSpondId() {
        return spondId;
    }

    public void setSpondId(String spondId) {
        this.spondId = spondId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Integer getSeatsPerTable() {
        return seatsPerTable;
    }

    public void setSeatsPerTable(Integer seatsPerTable) {
        this.seatsPerTable = seatsPerTable;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Set<Date> getDates() {
        return dates;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public List<Tafel> getTables() {
        return tables;
    }

    public Tafel AddTable(Tafel t){
        this.tables.add(t);
        return t;
    }

    public Tafel RemoveTable(Tafel t){
        this.tables.remove(t);
        return t;
    }

    public Tafel GetTable(Tafel t){
        this.tables.add(t);
        return tables.stream().filter(table -> table.getId()==t.getId()).findFirst().orElse(null);
    }

    public Ticket AddTicket(Ticket t) {
        this.tickets.add(t);
        return t;
    }

    public Ticket DeleteTicket(Ticket t) {
        this.tickets.remove(t);
        return t;
    }

    public Ticket GetTicket(Ticket t){
        return this.tickets.stream().filter(ticket->ticket.getId()==t.getId()).findFirst().orElse(null);
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void SpondUpdate(Event e){
        this.startTime = e.startTime;
        this.endTime = e.endTime;
        this.title = e.title;
        this.location = e.location;
        if(e.description!=null){
            this.description = e.description;
        }
    }

    public void CreateLayout(){
        if(this.tickets.isEmpty()){
            if (LayoutFormatter()) {
                // Height and length successfully added
                LayoutFormatter();
                System.out.println("created layout");
            } else {
                System.out.println("Failed to create layout");
            }
        }
    }

    private Boolean LayoutFormatter() {
        if (layout == null || layout.isEmpty()) {
            System.out.println("Layout is null or empty.");
            return false;
        }
        layout = layout.trim().toLowerCase();
        String[] array = layout.split("x");
        if (array.length != 2) {
            System.out.println("Layout format is invalid. Expected format: 'AxB', got: " + layout);
            return false;
        }
        try {
            this.kolommen = Integer.parseInt(array[0]);
            this.rijen = Integer.parseInt(array[1]);
            return true; // Successfully parsed
        } catch (NumberFormatException e) {
            System.out.println("Error parsing layout: " + e.getMessage());
            return false; // Parsing failed
        }
    }
}
