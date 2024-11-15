import React, { useEffect, useContext } from "react";
import { NavLink, useParams, useNavigate } from "react-router-dom";
import { deleteEventById, getEventById } from "../services/ApiService";
import { EventContext } from "../context/EventContext";
import styles from "../css/entity.module.css";

export default function ShowEvent() {
  const { id } = useParams();
  const { event, updateEvent, removeEventById } = useContext(EventContext);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      try {
        const fetchedEvent = await getEventById(id);
        updateEvent(fetchedEvent);
      } catch (error) {
        console.error("Error fetching events:", error);
      }
    }

    fetchData();
  }, [id, updateEvent]);

  async function deleteEvent() {
    try {
      await deleteEventById(id);
      removeEventById(id);
      navigate("/");
    } catch (error) {
      console.error("Error deleting event:", error);
    }
  }

  if (!event) {
    return <p>Loading event details...</p>;
  }

  // Helper function to format time
  const formatTime = (dateString) => {
    const date = new Date(dateString);
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${hours}:${minutes}`;
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString); // Convert to Date object
    const day = String(date.getDate()).padStart(2, '0'); // Ensure two digits
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  };

  return (
    <main>
        <div className={styles.post_item}>
            <div className={styles.post_content}>
                <h1>{event.title}</h1>
                <br />

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
                    <br />
                </>
                )}

                <h2>Datum</h2>
                <p>{formatDate(event.startTime)}</p>
                <br />

                <h2>Locatie en Tijd</h2>
                <p>Locatie: {event.location}</p>
                <p>Begin: {formatTime(event.startTime)}</p>
                <p>Einde: {formatTime(event.endTime)}</p>

                {event.spondId && (
                    <button style={{ border: "none", backgroundColor:'transparent' }}>
                        <a
                        href={`https://spond.com/client/sponds/${event.spondId}`}
                        target="_blank"
                        rel="noopener noreferrer"
                        >
                        <img
                            className="Spond"
                            src="/images/spond.png"
                            alt="Spond-logo"
                        />
                        </a>
                    </button>
                    )}

                <br />

                {event.spondId && (<div className="row">
                <button className={styles.updateBtn}>
                    <NavLink to={`/${id}/edit`}>
                    Aanpassen
                    </NavLink>
                </button>
                <div className="col-6 text-end">
                    <button
                    onClick={() => {
                        if (window.confirm("Ben je zeker dat je dit evenement wilt verwijderen?")) {
                        deleteEvent();
                        }
                    }}
                    className={styles.deleteBtn}
                    >
                    Verwijderen
                    </button>
                </div>
                </div>
                )}

                <br />
            </div>
        </div>
    </main>
  );
}
