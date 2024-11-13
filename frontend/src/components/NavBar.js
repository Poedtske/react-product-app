import { NavLink } from "react-router-dom";
import { useState } from "react";

export default function NavBar() {
  const [isNavExpanded, setIsNavExpanded] = useState(false);

  const handleToggle = () => {
    setIsNavExpanded(!isNavExpanded);
  };

  return (
    <header>
      <button
        className="hamburger"
        aria-expanded={isNavExpanded}
        aria-controls="main-nav"
        onClick={handleToggle}
      >
        <div className="bar"></div>
      </button>
      
      <a href="#" className="logo">
        <img src="/images/logos/logosFanfare/logoFanfare.png" alt="Fanfare Logo" />
      </a>
      
      <nav id="main-nav" aria-expanded={isNavExpanded}>
        <NavLink 
          exact 
          to="/" 
          activeClassName="active" 
          onClick={handleToggle}
        >
          Home
        </NavLink>
        
        <div className="dropdown" aria-expanded="false">
          <button className="btn2 dropbtn">Fanfare</button>
          <div className="dropdown-content">
            <NavLink 
              to="/fanfare/instrumenten" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Instrumenten
            </NavLink>
            <NavLink 
              to="/fanfare/geschiedenis" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Geschiedenis
            </NavLink>
            <NavLink 
              to="/fanfare/bestuur" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Bestuur
            </NavLink>
            <NavLink 
              to="/fanfare/dirigent" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Dirigent
            </NavLink>
          </div>
        </div>
        
        <NavLink 
          to="/jeugd" 
          activeClassName="active" 
          onClick={handleToggle}
        >
          Jeugd
        </NavLink>
        
        <div className="dropdown">
          <button className="dropbtn">Praktische Info</button>
          <div className="dropdown-content">
            <NavLink 
              to="/praktischeInfo/belangrijkeDocumenten" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Belangrijke Documenten
            </NavLink>
            <NavLink 
              to="/praktischeInfo/privacyverklaring" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Privacyverklaring
            </NavLink>
          </div>
        </div>
        
        <NavLink 
          to="/sponsors" 
          activeClassName="active" 
          onClick={handleToggle}
        >
          Sponsors
        </NavLink>
        
        <NavLink 
          to="/kalender" 
          activeClassName="active" 
          onClick={handleToggle}
        >
          Kalender
        </NavLink>
        
        {/* Authenticated Links */}
        { /* Mocking an authenticated state; replace with actual auth logic */ }
        {/* {true ? (
          <>
            <NavLink 
              to="/logout" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Logout
            </NavLink>
            <NavLink 
              to="/dashboard" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              <img className="avatar" src="/path/to/user/avatar.png" alt="User Avatar" />
            </NavLink>
          </>
        ) : (
          <>
            <NavLink 
              to="/register" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Register
            </NavLink>
            <NavLink 
              to="/login" 
              activeClassName="active" 
              onClick={handleToggle}
            >
              Login
            </NavLink>
          </>
        )} */}
      </nav>
    </header>
  );
}
