package com.example.backend.service.impl;

import com.example.backend.model.Event;
import com.example.backend.model.Tafel;
import com.example.backend.repository.EventDao;
import com.example.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDao eventRepository;


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
        if(!e.getLayout().equals(updatedEvent.getLayout())){
            e.setLayout(updatedEvent.getLayout());
            e.CreateLayout();
        }
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







}
