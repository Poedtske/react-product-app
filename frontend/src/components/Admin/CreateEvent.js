import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createEvent } from '../../services/ApiService'; // Assuming createEvent is imported

const CreateEvent = () => {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    date: "",
    location: "",
  });
  const navigate = useNavigate();  // Use the navigate hook

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Call the createEvent function and pass formData as the payload
      const createdEvent = await createEvent(formData); 

      // Assuming the event returned from the API contains an 'id' field
      navigate(`/api/admin/events/${createdEvent.id}`); // Navigate to the newly created event page
    } catch (error) {
      console.error('Error creating event:', error);  // Handle errors
    }
  };

  // Handle input field changes and update state
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <main>
      <form onSubmit={handleSubmit}>
        <h1>Create Event</h1>
        <input 
          name="title" 
          value={formData.title} 
          onChange={handleChange} 
          placeholder="Title" 
          required 
        />
        <textarea 
          name="description" 
          value={formData.description} 
          onChange={handleChange} 
          placeholder="Description" 
        />
        <input 
          name="date" 
          type="date" 
          value={formData.date} 
          onChange={handleChange} 
          required 
        />
        <input 
          name="location" 
          value={formData.location} 
          onChange={handleChange} 
          placeholder="Location" 
        />
        <button type="submit">Create</button>
      </form>
    </main>
  );
};

export default CreateEvent;
