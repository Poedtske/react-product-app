import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import styles from '../home/home.module.css';
import { getEvents } from '../../services/ApiService';

const Home = () => {
  const [events, setEvents] = useState([]); // Store events data
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

  // Function to find the closest event (based on date) to today
  const getClosestEvent = (events) => {
    if (!Array.isArray(events) || events.length === 0) {
      return null;
    }

    const today = new Date();
    let closestEvent = null;
    let closestDate = null;

    events.forEach((event) => {
      event.dates.forEach((dateObj) => {
        const eventDate = new Date(`${dateObj.date}T${dateObj.startTime}`);
        if (eventDate >= today && (!closestDate || eventDate < closestDate)) {
          closestDate = eventDate;
          closestEvent = { ...event, currentEventDate: dateObj };
        }
      });
    });

    return closestEvent;
  };

  // Get the closest event to today
  const firstEvent = getClosestEvent(events);

  // Function to start auto sliding the event every 5 seconds
  const startAutoSlide = () => {
    if (!intervalId) {
      const id = setInterval(nextActivity, 5000);
      setIntervalId(id);
    }
  };

  // Stop the auto slide
  const stopAutoSlide = () => {
    if (intervalId) {
      clearInterval(intervalId);
      setIntervalId(null);
    }
  };

  // Function to go to the next activity/event
  const nextActivity = () => {
    setCurrentImageIndex((prevIndex) =>
      prevIndex === events.length - 1 ? 0 : prevIndex + 1
    );
  };

  // Function to go to the previous activity/event
  const prevActivity = () => {
    setCurrentImageIndex((prevIndex) =>
      prevIndex === 0 ? events.length - 1 : prevIndex - 1
    );
  };

  // Start the auto sliding when events are available
  useEffect(() => {
    if (events.length > 0) {
      startAutoSlide();
    }
    return () => {
      clearInterval(intervalId);
    };
  }, [events]);

  // User interactions with the slider stop auto sliding temporarily
  const handleNext = () => {
    nextActivity();
    stopAutoSlide();
  };

  const handlePrev = () => {
    prevActivity();
    stopAutoSlide();
  };

  // Helper function to format date to 'DD/MM/YYYY'
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  };

  // Helper function to format time for spond events to 'HH:mm'
  const formatTimeSpond = (dateString) => {
    const date = new Date(dateString);
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${hours}:${minutes}`;
  };

  // Helper function to format time for regular events to 'HH:mm'
  const formatTime = (timeString) => {
    const [hours, minutes] = timeString.split(':');
    return `${hours}:${minutes}`;
  };

  // Function to get event details
  const getEventDetails = (event) => {
    if (event.spondId) {
      // For spondEvents (events with spondId), use startTime directly
      return (
        <>
          <p className={styles.p} id="naam">{event.title}</p>
          <p className={styles.p} id="datum">{formatDate(event.startTime)}</p>
          <p className={styles.p} id="uur">{formatTimeSpond(event.startTime)}</p>
          <p className={styles.p} id="locatie">{event.location}</p>
        </>
      );
    } else if (event.dates && event.dates.length > 0) {
      // For regular events with multiple dates
      const firstDate = event.dates[0]; // You can adjust this logic to pick a specific date
      return (
        <>
          <p className={styles.p} id="naam">{event.title}</p>
          <p className={styles.p} id="datum">{formatDate(firstDate.date)}</p>
          <p className={styles.p} id="uur">{formatTime(firstDate.startTime)}</p>
          <p className={styles.p} id="locatie">{event.location}</p>
        </>
      );
    }
    return null; // Return null if no valid event data is found
  };

  return (
    <main className={styles.main}>
      {/* Banner Image */}
      <img src="/images/banner.jpeg" alt="banner" id="banner" />

      {/* Display first event poster if available */}
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

      {/* Text Section */}
      <section className={styles.section} style={{ backgroundColor: 'gray' }}>
        <p className={styles.p}>
          Wij zijn een groep enthousiaste amateurmuzikanten bestaande uit momenteel een dertigtal
          spelende leden waarvan de leeftijden uiteenlopend zijn, gaande van kinderen tot
          gepensioneerden. Jong of oud, het maakt voor ons niet uit!!!
        </p>
        <p className={styles.p}>
          Het bestuur bestaat uit gemotiveerde leden, die allen instaan voor de bevordering van onze
          fanfare.
        </p>
        <p className={styles.p}>
          Onze fanfare is altijd op zoek naar nieuwe leden, die interesse hebben in muziek. Ben je
          -18, beginnende muzikant of een ervaren speler, wees er dan maar zeker van dat je altijd
          welkom bent om onze rangen te komen bijvullen.
        </p>
        <p className={styles.p}>
          Indien je niets of bijna niets van muziek kent, geen nood, dan is er de mogelijkheid om
          lessen aan te bieden alsook een instrument gratis in bruikleen te krijgen.
        </p>
        <p className={styles.p}>
          Het belangrijkste voor onze fanfare is dat deze hobby een unieke kijk op de samenleving
          geeft, jong en oud die samenspelen en samen plezier hebben met de muziek die ze maken.
        </p>
        <p className={styles.p}>
          We repeteren op vrijdagavond in de feestzaal van de Borgt van 20 tot 22 uur.
          Geïnteresseerden zijn altijd welkom om langs te komen!
        </p>
      </section>

      {/* Event Slider */}
      <section className={styles.activity} style={{ backgroundColor: 'white', color: 'black', textAlign: 'center' }}>
        {events.length > 0 && getEventDetails(events[currentImageIndex])}

        <div>
          <button style={{ width: '10em' }}>
            <Link to="/kalender">Kalender</Link>
          </button>
        </div>

        <div>
          <button style={{ width: '2em', marginRight: '10px' }} className="prev" onClick={handlePrev}>
            &#10094;
          </button>

          <button style={{ width: '2em' }} className="next" onClick={handleNext}>
            &#10095;
          </button>
        </div>
      </section>

      {/* Sponsor Section */}
      <section className={styles.section} style={{ width: '80%', maxWidth: '600px' }}>
        <button>
          <a
            href="https://www.trooper.be/nl/trooperverenigingen/kfdemoedigevrienden"
            target="_blank"
            rel="noopener noreferrer"
          >
            <img className="fotos" src="/images/trooper_logo.png" alt="Trooper" />
          </a>
        </button>
      </section>
    </main>
  );
};

export default Home;
