import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'; // For routing
import styles from '../home/home.module.css' // You can import the CSS file specific to this page
import {getEvents, getProducts} from '../../services/ApiService';

const Home = () => {
  const [events, setEvents] = useState([]); // Store events data
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [intervalId, setIntervalId] = useState(null);

  useEffect(() => {
    let fetchData = async () => {
      try {
        const events = await getEvents(); // assuming getEvents fetches events from your backend
        if (Array.isArray(events)) {
          setEvents(events); // Only update state if events is an array
        } else {
          console.error('Fetched data is not an array:', events);
        }
      } catch (error) {
        console.error('Error fetching events:', error);
      }
    };
    fetchData();
  }, []);
  

  // Function to find the closest event (based on date) to today
  const getClosestEvent = (events) => {
    // Ensure events is an array before proceeding
    if (!Array.isArray(events) || events.length === 0) {
      return null; // Return null if no events are available
    }
  
    const today = new Date();
    return events
      .filter((event) => event.start_time) // Ensure event has a start time
      .reduce((closest, event) => {
        const eventDate = new Date(event.start_time);
        const closestDate = new Date(closest.start_time);
        return eventDate >= today && eventDate < closestDate ? event : closest;
      }, events[0]); // Start with the first event if no closer one is found
  };
  

  // Helper function to format date to 'DD/MM/YYYY'
  const formatDate = (dateString) => {
    const date = new Date(dateString); // Convert to Date object
    const day = String(date.getDate()).padStart(2, '0'); // Ensure two digits
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  };

  // Get the closest event to today
  const firstEvent = getClosestEvent(events);

  // Helper function to format time to 'HH:mm'
  const formatTime = (dateString) => {
    const date = new Date(dateString);
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${hours}:${minutes}`;
  };

  // Function to start auto sliding the event every 5 seconds
const startAutoSlide = () => {
  // Only start a new interval if one is not already running
  if (!intervalId) {
    const id = setInterval(nextActivity, 5000); // Slide every 5 seconds
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
  setCurrentImageIndex((prevIndex) => (prevIndex === events.length - 1 ? 0 : prevIndex + 1));
};

// Function to go to the previous activity/event
const prevActivity = () => {
  setCurrentImageIndex((prevIndex) => (prevIndex === 0 ? events.length - 1 : prevIndex - 1));
};

// Start the auto sliding when events are available
useEffect(() => {
  if (events.length > 0) {
    startAutoSlide(); // Start the auto slide interval
  }
  return () => {
    clearInterval(intervalId); // Clear the interval when the component is unmounted
  };
}, [events]);

// When the user interacts with the slider, stop the auto sliding for a while
const handleNext = () => {
  nextActivity();
  stopAutoSlide(); // Stop auto slide on interaction
  
};

const handlePrev = () => {
  prevActivity();
  stopAutoSlide(); // Stop auto slide on interaction
  
};

  return (
    <main  className={styles.main}>
      {/* Banner Image */}
      <img src="/images/banner.jpeg" alt="banner" id="banner" />

      {/* Display first event poster if available */}
      {firstEvent && firstEvent.poster && (
        <section className={styles.section} style={{ backgroundColor: 'black' }}>
          <Link to={`/events/${firstEvent.id}`} style={{ marginInline: 'auto', width: '30%' }}>
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
          Wij zijn een groep enthousiaste amateurmuzikanten bestaande uit momenteel een dertigtal spelende leden waarvan de leeftijden uiteenlopend zijn,
          gaande van kinderen tot gepensioneerden.
          Jong of oud, het maakt voor ons niet uit!!!
        </p>
        <p className={styles.p}>
            Het bestuur bestaat uit gemotiveerde leden, die allen instaan voor de bevordering van onze fanfare.
        </p>
        <p className={styles.p}></p>
        <p className={styles.p}></p>
        <p className={styles.p}></p>
        <p className={styles.p}>
            Onze fanfare is altijd op zoek naar nieuwe leden, die interesse hebben in muziek.
            Ben je -18, beginnende muzikant of een ervaren speler,
            wees er dan maar zeker van dat je altijd welkom bent om onze rangen te komen bijvullen.
        </p>
        <p className={styles.p}>
            Indien je niets of bijna niets van muziek kent, geen nood,
            dan is er de mogelijkheid om lessen aan te bieden alsook een instrument gratis in bruikleen te krijgen.
        </p>
        <p className={styles.p}>
            Het belangrijkste voor onze fanfare is dat deze hobby een unieke kijk op de samenleving geeft,
            jong en oud die samenspelen en samen plezier hebben met de muziek die ze maken.
        </p>
        <p className={styles.p}>
            We repeteren op vrijdagavond in de feestzaal van de Borgt van 20 tot 22 uur. Geïnteresseerden zijn altijd welkom om langs te komen!
        </p>
        <p className={styles.p}>
          Wij zijn een groep enthousiaste amateurmuzikanten bestaande uit momenteel een dertigtal spelende leden waarvan de leeftijden uiteenlopend zijn, gaande van kinderen tot gepensioneerden.
          Jong of oud, het maakt voor ons niet uit!!!
        </p>
      </section>

      {/* Event Slider */}
      <section className={styles.activity} style={{ backgroundColor: 'white', color: 'black', textAlign: 'center' }}>
        {events.length > 0 && (
          <>
            <p className={styles.p} id="naam">{events[currentImageIndex].title}</p>
            <p className={styles.p} id="datum">{formatDate(events[currentImageIndex].startTime)}</p>
            <p className={styles.p} id="uur">{formatTime(events[currentImageIndex].startTime)}</p>
            <p className={styles.p} id="locatie">{events[currentImageIndex].location}</p>
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

            <div>
              <button style={{ width: '10em' }}>
                <Link to="/kalender">Kalender</Link>
              </button>
            </div>

            <div>
              <button 
                style={{ width: '2em', marginRight: '10px' }}  // Add margin-right for spacing
                className="prev" 
                onClick={handlePrev}
              >
                &#10094;
              </button>
              
              <button 
                style={{ width: '2em' }} 
                className="next" 
                onClick={handleNext}
              >
                &#10095;
              </button>
            </div>

          </>
        )}
      </section>

      {/* Sponsor Section */}
      <section className={styles.section} style={{ width: '80%', maxWidth: '600px' }}>
        <button>
          <a href="https://www.trooper.be/nl/trooperverenigingen/kfdemoedigevrienden" target="_blank" rel="noopener noreferrer">
            <img className="fotos" src="/images/trooper_logo.png" alt="Trooper" />
          </a>
        </button>
      </section>
    </main>
  );
};

export default Home;
