import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import styles from '../../pages/kalender/kalender.module.css'; // Import CSS module
import { getEvents } from '../../services/ApiService';

const Kalender = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const events = await getEvents(); // Fetch events
        setEvents(events);
        setLoading(false);
      } catch (error) {
        setError("Error fetching events");
        setLoading(false);
        console.error("Error fetching events:", error);
      }
    };
    fetchData();
  }, []);

  const formatDate = (dateString, fallbackDates) => {
    // If dateString is null or empty, use the first date's startTime from fallbackDates
    if (!dateString && fallbackDates?.length > 0) {
      dateString = fallbackDates[0]?.startTime;
    }

    if (!dateString) return { day: "--", month: "N/A" }; // Handle edge case

    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, "0");
    const month = date.toLocaleString("en", { month: "short" }).toUpperCase();
    return { day, month };
  };

  if (loading) {
    return <p>Loading events...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <main className={styles.main}>
      <div className={styles.eventList}>
        <div className={styles.eventContainer}>
          {events.map((event) => {
            const { day, month } = formatDate(event.startTime, event.dates);

            return (
              <Link
                to={{
                  pathname: `/admin/events/${event.id}`,
                  state: { event }, // Pass event data as state
                }}
                key={event.id}
                className={styles.event}
              >
                <div className={styles.dateContainer}>
                  <div className={styles.dateDay}>{day}</div>
                  <div className={styles.dateMonth}>{month}</div>
                </div>
                <div className={styles.eventDetails}>
                  <div className={styles.eventTitle}>{event.title}</div>
                  <div className={styles.eventLocation}>{event.location}</div>
                </div>
              </Link>
            );
          })}
        </div>
      </div>
    </main>
  );
};

export default Kalender;
