package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EventDate {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date date;  // In dd/MM/yyyy format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS", timezone = "UTC")
    private Date startTime;  // In HH:mm format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS", timezone = "UTC")
    private Date endTime;  // In HH:mm format

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure ID is auto-generated
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;  // This maps the EventDate to a specific Event

    // Constructor for creating EventDate objects
    public EventDate(Date date, Date startTime, Date endTime, Event event) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
    }

}
