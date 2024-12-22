import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import styles from '../home/home.module.css';
import { getEvents } from '../../services/ApiService';

const Home = () => {
  const [events, setEvents] = useState([]);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [intervalId, setIntervalId] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const fetchedEvents = await getEvents();
        if (Array.isArray(fetchedEvents)) {
          setEvents(fetchedEvents);
        } else {
          console.error('Fetched data is not an array:', fetchedEvents);
        }
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    };
    fetchData();
  }, []);

  const getClosestEvent = (events) => {
    if (!Array.isArray(events) || events.length === 0) {
      return null;
    }

    const today = new Date();
    let closestEvent = null;
    let closestDate = null;

    events.forEach((event) => {
      if (event.dates && Array.isArray(event.dates)) {
        event.dates.forEach((dateObj) => {
          const eventDate = new Date(`${dateObj.date}T${dateObj.startTime}`);
          if (eventDate >= today && (!closestDate || eventDate < closestDate)) {
            closestDate = eventDate;
            closestEvent = { ...event, currentEventDate: dateObj };
          }
        });
      }
    });

    return closestEvent;
  };

  const firstEvent = getClosestEvent(events);

  const startAutoSlide = () => {
    if (!intervalId) {
      const id = setInterval(nextActivity, 5000);
      setIntervalId(id);
    }
  };

  const stopAutoSlide = () => {
    if (intervalId) {
      clearInterval(intervalId);
      setIntervalId(null);
    }
  };

  const nextActivity = () => {
    setCurrentImageIndex((prevIndex) =>
      prevIndex === events.length - 1 ? 0 : prevIndex + 1
    );
  };

  const prevActivity = () => {
    setCurrentImageIndex((prevIndex) =>
      prevIndex === 0 ? events.length - 1 : prevIndex - 1
    );
  };

  useEffect(() => {
    if (events.length > 0) {
      startAutoSlide();
    }
    return () => {
      clearInterval(intervalId);
    };
  }, [events]);

  const handleNext = () => {
    nextActivity();
    stopAutoSlide();
  };

  const handlePrev = () => {
    prevActivity();
    stopAutoSlide();
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  };

  const formatTimeSpond = (dateString) => {
    const date = new Date(dateString);
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${hours}:${minutes}`;
  };

  const getEventDetails = (event) => {
    if (!event) return null;

    return (
      <>
        <p className={styles.p} id="naam">{event.title}</p>
        <p className={styles.p} id="datum">{event.startTime ? formatDate(event.startTime) : 'Geen datum'}</p>
        <p className={styles.p} id="uur">{event.startTime ? formatTimeSpond(event.startTime) : 'Geen tijd'}</p>
        <p className={styles.p} id="locatie">{event.location || 'Geen locatie'}</p>
      </>
    );
  };

  return (
    <main className={styles.main}>
      <img src="/images/banner.jpeg" alt="banner" id="banner" />

      {firstEvent && firstEvent.poster && (
        <section className={styles.section} style={{ backgroundColor: 'black' }}>
          <Link
            to={`/events/${firstEvent.spondId || firstEvent.id}`}
            style={{ marginInline: 'auto', width: '30%' }}
          >
            <img
              width="100%"
              id="poster"
              src={firstEvent.poster}
              alt={`${firstEvent.title}_poster`}
            />
          </Link>
        </section>
      )}

      <section className={styles.activity} style={{ backgroundColor: 'white', color: 'black', textAlign: 'center' }}>
        {events.length > 0 && getEventDetails(events[currentImageIndex])}

        {events[currentImageIndex] && (
          <Link
            to={`/events/${events[currentImageIndex].id}`}
            id="eventLink"
            style={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              width: '2.2em',
              height: '2.2em',
              marginLeft: 'auto',
              marginRight: 'auto',
              borderRadius: '50%',
              backgroundColor: 'transparent',
              border: 'solid black 2px',
            }}
          >
            <i className="fa-solid fa-info" style={{ fontSize: '1.2em', color: 'black' }}></i>
          </Link>
        )}

        <div>
          <button style={{ width: '2em', marginRight: '10px' }} onClick={handlePrev}>&#10094;</button>
          <button style={{ width: '2em' }} onClick={handleNext}>&#10095;</button>
        </div>
      </section>
    </main>
  );
};

export default Home;
