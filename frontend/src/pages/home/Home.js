import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'; // For routing
import styles from '../home/home.module.css' // You can import the CSS file specific to this page

const Home = () => {
  const [events, setEvents] = useState([]); // Store events data
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  // Replace this with actual fetching logic from an API
  useEffect(() => {
    // Example of fetching events
    fetch('/api/events') // Replace with your actual endpoint
      .then((response) => response.json())
      .then((data) => setEvents(data))
      .catch((error) => console.error('Error fetching events:', error));
  }, []);

  const formatDate = (dateString) => {
    const [year, month, day] = dateString.split('-');
    return `${day}/${month}/${year}`;
  };

  const firstEvent = events[0] || {};

  const nextActivity = () => {
    setCurrentImageIndex((prevIndex) => (prevIndex === events.length - 1 ? 0 : prevIndex + 1));
  };

  const prevActivity = () => {
    setCurrentImageIndex((prevIndex) => (prevIndex === 0 ? events.length - 1 : prevIndex - 1));
  };

  useEffect(() => {
    const slideInterval = setInterval(nextActivity, 5000); // Slide every 5 seconds

    return () => clearInterval(slideInterval); // Clean up interval on component unmount
  }, [events]);

  return (
    <main  className={styles.main}>
      {/* Banner Image */}
      <img src="/images/banner.jpeg" alt="banner" id="banner" />

      {/* Display first event poster if available */}
      {firstEvent.poster && (
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
      <section className={styles.section} style={{ backgroundColor: 'white', color: 'black' }}>
        {events.length > 0 && (
          <>
            <p className={styles.p} id="naam">{events[currentImageIndex].title}</p>
            <p className={styles.p} id="datum">{formatDate(events[currentImageIndex].date)}</p>
            <p className={styles.p} id="uur">{events[currentImageIndex].start_time.slice(0, -3)}</p>
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
              <button style={{ width: '2em' }} className="prev" onClick={prevActivity}>
                &#10094;
              </button>
              <button style={{ width: '2em' }} className="next" onClick={nextActivity}>
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
