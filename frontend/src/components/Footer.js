import React from 'react';

export default function Footer() {
  // Mock data for the sponsor - replace this with actual data fetching if needed
  const sponsor = {
    rank: 1,
    url: "https://sponsor-website.com",
    logo: "/path/to/sponsor/logo.png",
  };

  return (
    <footer>
      {/* Sponsor Section */}
      {sponsor && sponsor.rank === 1 ? (
        <button>
          {sponsor.url ? (
            <a href={sponsor.url} target="_blank" rel="noopener noreferrer">
              <img className="fotos" src={sponsor.logo} alt="Hoofdsponsor" />
            </a>
          ) : (
            <img className="fotos" src={sponsor.logo} alt="Hoofdsponsor" />
          )}
        </button>
      ) : (
        <button></button>
      )}

      {/* Contact Email */}
      <div id="mail">
        k.f.demoedigevrienden@gmail.com
      </div>

      {/* Social Media Links */}
      <nav>
        <a href="https://www.facebook.com/groups/284633908275549" target="_blank" rel="noopener noreferrer">
          <i className="fab fa-facebook"></i>
        </a>
        {/* Example of additional link - uncomment if needed */}
        {/* 
        <a href="https://spond.com/client/groups/4B54CE00ED8F4936840114345105B38C" target="_blank" rel="noopener noreferrer">
          <img className="Spond" src="/path/to/spond-logo.png" alt="Spond-logo" />
        </a> 
        */}
        <a href="https://www.instagram.com/instaporkestdmv/" target="_blank" rel="noopener noreferrer">
          <i className="fab fa-instagram"></i>
        </a>
      </nav>
    </footer>
  );
}
