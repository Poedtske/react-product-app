// import React, { useEffect, useState } from "react";
// import { useNavigate, useParams } from "react-router-dom";
// import { updateEventById } from '../../services/ApiService';

// const EditEvent = () => {
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);
//   const { id } = useParams();
//   const navigate = useNavigate();
//   const [formData, setFormData] = useState({
//     title: "",
//     description: "",
//     date: "",
//     location: "",
//   });


//   const handleSubmit = async(e) => {
//     e.preventDefault();
//     try {
//         const events = await updateEventById(); // Fetch events
//         //setEvents(events);
//         setLoading(false);
//         navigate(`/api/admin/events/${event.id}`);
//       } catch (error) {
//         setError('Error fetching events');
//         setLoading(false);
//         console.error('Error fetching events:', error);
//       }
//   };

//   const handleChange = (e) => {
//     setFormData({ ...formData, [e.target.name]: e.target.value });
//   };

//   return (
//     <main>
//         <form onSubmit={handleSubmit}>
//       <h1>Edit Event</h1>
//       <input name="title" value={formData.title} onChange={handleChange} placeholder="Title" required />
//       <textarea name="description" value={formData.description} onChange={handleChange} placeholder="Description" />
//       <input name="date" type="date" value={formData.date} onChange={handleChange} required />
//       <input name="location" value={formData.location} onChange={handleChange} placeholder="Location" />
//       <button type="submit">Save</button>
//     </form>
//     </main>
//   );
// };

// export default EditEvent;
