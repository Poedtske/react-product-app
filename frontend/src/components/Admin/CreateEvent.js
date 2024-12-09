import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Box,
  Typography,
  TextField,
  Select,
  MenuItem,
  Button,
} from "@mui/material";
import { createEvent, getEventTypes } from "../../services/ApiService"; // Import API functions
import { format } from 'date-fns'; // Import date-fns for date formatting

const CreateEvent = () => {
  const [eventTypes, setEventTypes] = useState([]);
  const [formData, setFormData] = useState({
    title: "",
    location: "",
    type: "",
    description: "",
    seatsPerTable: "",
    layout: "",
    dates: [{ date: "", startTime: "", endTime: "" }], // Store multiple dates
  });

  const [errors, setErrors] = useState({});
  const [submitStatus, setSubmitStatus] = useState("");
  const navigate = useNavigate();

  // Fetch event types on component mount
  useEffect(() => {
    const fetchEventTypes = async () => {
      try {
        const types = await getEventTypes();
        console.log("API Response:", types);  // Log the full response
        
        // Filter out "UNKNOWN" types if present
        const filteredTypes = types.filter((type) => type !== "UNKNOWN");
        console.log("Filtered event types:", filteredTypes);  // Log filtered types
  
        setEventTypes(filteredTypes); // Update the state with filtered types
      } catch (error) {
        console.error("Error fetching event types:", error);
      }
    };
  
    fetchEventTypes();
  }, []);
  
  const handleChange = (e, index) => {
    const { name, value } = e.target;
    const updatedDates = [...formData.dates];
    updatedDates[index] = { ...updatedDates[index], [name]: value };
    setFormData({ ...formData, dates: updatedDates });
  };

  const handleAddDate = () => {
    setFormData({
      ...formData,
      dates: [...formData.dates, { date: "", startTime: "", endTime: "" }],
    });
  };

  const handleRemoveDate = (index) => {
    const updatedDates = formData.dates.filter((_, i) => i !== index);
    setFormData({ ...formData, dates: updatedDates });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = {};
    if (!formData.type) validationErrors.type = "Type is required.";
  
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      const event = await createEvent(formData);
      setSubmitStatus("Event created successfully!");
      setErrors({});
      navigate(`/admin/events/${event.id}`);
    } catch (error) {
      setSubmitStatus("Failed to create event. Please try again.");
      console.error("Error creating event:", error);
    }
  };

  const formatDateTime = (date, time) => {
    const dateTime = new Date(`${date}T${time}`);
    return format(dateTime, "yyyy-MM-dd HH:mm:ss.SSS");
  };

  return (
    <Container
      maxWidth="xs"
      sx={{
        backgroundColor: "black",
        color: "white",
        borderRadius: 2,
        padding: 3,
        boxShadow: 3,
      }}
    >
      <Box
        component="form"
        onSubmit={handleSubmit}
        sx={{
          mt: 4,
          display: "flex",
          flexDirection: "column",
          gap: 2,
        }}
      >
        <Typography variant="h5" component="h1" align="center" sx={{ color: "white" }}>
          Create Event
        </Typography>

        {submitStatus && (
          <Typography
            variant="body2"
            align="center"
            sx={{ color: submitStatus.includes("success") ? "green" : "red" }}
          >
            {submitStatus}
          </Typography>
        )}

        {Object.values(errors).length > 0 && (
          <Typography variant="body2" color="error" align="center" sx={{ color: "red" }}>
            {Object.values(errors).join(" \\n")}
          </Typography>
        )}

        <TextField
          label="Title"
          name="title"
          value={formData.title}
          onChange={(e) => setFormData({ ...formData, title: e.target.value })}
          fullWidth
          required
          InputLabelProps={{
            style: { color: "white" },
          }}
          InputProps={{
            style: { color: "white", backgroundColor: "#333" },
          }}
        />

        <TextField
          label="Location"
          name="location"
          value={formData.location}
          onChange={(e) => setFormData({ ...formData, location: e.target.value })}
          fullWidth
          required
          InputLabelProps={{
            style: { color: "white" },
          }}
          InputProps={{
            style: { color: "white", backgroundColor: "#333" },
          }}
        />

        <Select
          name="type"
          value={formData.type}
          onChange={(e) => setFormData({ ...formData, type: e.target.value })}
          displayEmpty
          fullWidth
          sx={{
            color: "white",
            backgroundColor: "#333",
            "& .MuiSelect-icon": { color: "white" },
          }}
        >
          <MenuItem value="" disabled>
            Select Type
          </MenuItem>
          {eventTypes.map((eventType) => (
            <MenuItem key={eventType} value={eventType}>
              {eventType}
            </MenuItem>
          ))}
        </Select>

        <TextField
          label="Description"
          name="description"
          value={formData.description}
          onChange={(e) => setFormData({ ...formData, description: e.target.value })}
          fullWidth
          required
          InputLabelProps={{
            style: { color: "white" },
          }}
          InputProps={{
            style: { color: "white", backgroundColor: "#333" },
          }}
        />

        <TextField
          label="Seats per Table"
          name="seatsPerTable"
          type="number"
          value={formData.seatsPerTable}
          onChange={(e) => setFormData({ ...formData, seatsPerTable: e.target.value })}
          fullWidth
          required
          InputLabelProps={{
            style: { color: "white" },
          }}
          InputProps={{
            style: { color: "white", backgroundColor: "#333" },
          }}
        />

        <TextField
          label="Layout (columns x rows, e.g., 5x5)"
          name="layout"
          value={formData.layout}
          onChange={(e) => setFormData({ ...formData, layout: e.target.value })}
          fullWidth
          required
          InputLabelProps={{
            style: { color: "white" },
          }}
          InputProps={{
            style: { color: "white", backgroundColor: "#333" },
          }}
        />

        {/* Dynamic Dates Section */}
        <Typography variant="h6" align="center" sx={{ color: "white" }}>
          Event Dates
        </Typography>

        {formData.dates.map((dateField, index) => (
          <Box key={index} sx={{ display: "flex", gap: 2, flexDirection: "column" }}>
            <TextField
              label="Date"
              name="date"
              value={dateField.date}
              onChange={(e) => handleChange(e, index)}
              fullWidth
              required
              InputLabelProps={{ style: { color: "white" } }}
              InputProps={{ style: { color: "white", backgroundColor: "#333" } }}
            />
            <TextField
              label="Start Time"
              name="startTime"
              type="time"
              value={dateField.startTime}
              onChange={(e) => handleChange(e, index)}
              fullWidth
              required
              InputLabelProps={{ style: { color: "white" } }}
              InputProps={{ style: { color: "white", backgroundColor: "#333" } }}
            />
            <TextField
              label="End Time"
              name="endTime"
              type="time"
              value={dateField.endTime}
              onChange={(e) => handleChange(e, index)}
              fullWidth
              required
              InputLabelProps={{ style: { color: "white" } }}
              InputProps={{ style: { color: "white", backgroundColor: "#333" } }}
            />
            <Button variant="outlined" color="error" onClick={() => handleRemoveDate(index)}>
              Remove Date
            </Button>
          </Box>
        ))}

        <Button variant="contained" onClick={handleAddDate} sx={{ mt: 2 }}>
          + Add Date
        </Button>

        <Button
          type="submit"
          variant="contained"
          fullWidth
          sx={{
            backgroundColor: "white",
            color: "black",
            "&:hover": {
              backgroundColor: "gray",
              color: "white",
            },
          }}
        >
          Add Event
        </Button>
      </Box>
    </Container>
  );
};

export default CreateEvent;
