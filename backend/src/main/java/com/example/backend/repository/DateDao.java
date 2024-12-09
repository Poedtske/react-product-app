package com.example.backend.repository;

import com.example.backend.model.Event;
import com.example.backend.model.EventDate;
import org.springframework.data.repository.CrudRepository;

public interface DateDao extends CrudRepository<EventDate,Long> {
}
