package com.example.backend.service;

import com.example.backend.model.Event;
import com.example.backend.model.Product;

import java.util.List;

public interface EventService {
    Event save(Event event);

    Event updateById(Long id, Event event);

    Iterable<Event> findAll();

    Event findById(Long id);

    Event findById(String id);

    void deleteById(Long id);

    void deleteById(String id);

    void deleteEvent(Event e);
}
