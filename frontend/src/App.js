import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import NavBar from './components/NavBar';
import Footer from './components/Footer';
import Home from './pages/home/Home';
import Bestuur from './pages/fanfare/bestuur/Bestuur';
import Dirigent from './pages/fanfare/dirigent/Dirigent';
import Geschiedenis from './pages/fanfare/geschiedenis/Geschiedenis';
import Instrumenten from './pages/fanfare/instrumenten/Instrumenten';
import Instaporkest from './pages/instaporkest/Instaporkest';
import Privacy from './pages/info/privacy/Privacy';
import Kalender from './pages/kalender/Kalender';
import Sponsors from './pages/sponsors/Sponsors';
import ShowEvent from './components/ShowEvent';
import './App.css';

import { EventProvider } from './context/EventContext';
import Krantje from "./pages/info/krantje/Krantje";

function App() {
  return (

    <>      
        <NavBar />
        <EventProvider>
          <Routes>
            <Route path="/events/:id" element={<ShowEvent/>} />
          </Routes>
        </EventProvider>        
        <Routes>    
          <Route path="/" element={<Home />} />      
          <Route path="/fanfare/bestuur" element={<Bestuur />} />
          <Route path="/fanfare/dirigent" element={<Dirigent />} />
          <Route path="/fanfare/geschiedenis" element={<Geschiedenis />} />
          <Route path="/fanfare/instrumenten" element={<Instrumenten />} />
          <Route path="/instaporkest" element={<Instaporkest />} />
          <Route path="/info/krantje" element={<Krantje />} />
          <Route path="/info/privacy" element={<Privacy />} /> 
          <Route path="/kalender" element={<Kalender />} />
          <Route path="/sponsors" element={<Sponsors />} />
        </Routes>
        <Footer/>
      
    </>
    
  );
}

export default App;
