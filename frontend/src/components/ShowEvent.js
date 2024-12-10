import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getEventById, deleteEventById } from "../services/ApiService"; // Import API functions
import styles from "../css/entity.module.css";

const ShowEvent = () => {
  const { id } = useParams(); // Get event ID from route params
  const navigate = useNavigate();

  const [event, setEvent] = useState(null); // State to hold event details
  const [loading, setLoading] = useState(true); // State to track loading
  const [error, setError] = useState(null); // State to track errors

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        const fetchedEvent = await getEventById(id); // Fetch event details from API
        setEvent(fetchedEvent); // Store the event in state
        setLoading(false); // Stop loading once data is fetched
      } catch (err) {
        console.error("Error fetching event:", err);
        setError("Failed to fetch event details. Please try again later.");
        setLoading(false); // Stop loading even if there's an error
      }
    };

    fetchEvent();
  }, [id]);

  const formatTime = (date) => {
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    return `${hours}:${minutes}`;
  };

  const formatDate = (date) => {
    const day = String(date.getDate()).padStart(2, "0");
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  };

  const renderDatesList = (dates) => {
    return (
      <ul>
        {dates.map((entry, index) => {
          const startDateTime = new Date(`${entry.date}T${entry.startTime}`);
          const endDateTime = new Date(`${entry.date}T${entry.endTime}`);
          return (
            <p>
              {formatDate(startDateTime)} - {formatTime(startDateTime)} to {formatTime(endDateTime)}
            </p>
          );
        })}
      </ul>
    );
  };

  if (loading) {
    return <p>Loading event details...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <main>
      <div className={styles.post_item}>
        <div className={styles.post_content}>
          <h1>{event.title}</h1>

          {event.poster && (
            <img
              style={{ marginInline: "auto" }}
              src={event.poster}
              alt={`${event.title}_poster`}
            />
          )}

          {event.description && (
            <>
              <h2>Omschrijving</h2>
              <p>{event.description}</p>
            </>
          )}
          <br/>
          <h2>Locatie</h2>
          <p>{event.location}</p>
          <br/>
          <h2>Datum & Tijd</h2> 
          {event.startTime && event.endTime ? (
            <>
                           
              <p>Begin: {formatTime(new Date(event.startTime))}</p>
              <p>Einde: {formatTime(new Date(event.endTime))}</p>
            </>
            
          ) : event.dates && event.dates.length > 0 ? (
            renderDatesList(event.dates)
          ) : (
            <p>Geen tijd & datum beschikbaar</p>
          )}
          <button
              className={styles.button}
              onClick={() => navigate(`/events/${id}/layout`)}
            >
              Koop Tickets
            </button>
        </div>
      </div>
    </main>
  );
};

export default ShowEvent;
