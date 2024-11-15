import React, { createContext, useState } from 'react';

export const EventContext = createContext();

export const EventProvider = ({ children }) => {
  const [events, setEvents] = useState([]);
  const [event, setEvent] = useState({});

  const updateEvents = (events) => {
    setEvents(events)
  }

  const addEvent = (event) => {
    setEvents([... events, event]);
  }

  const updateEvent = (event) => {
    setEvent(event);
  }

  const removeEventById = (id) => {
    const newEvents = events.filter((event) => event.id !== id);
    setEvents(newEvents);
  }

  return (
    <EventContext.Provider value={{ events, event, updateEvents, updateEvent, removeEventById, addEvent }}>
      {children}
    </EventContext.Provider>
  );
}