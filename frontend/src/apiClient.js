import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:5000", // Replace with your backend proxy server URL
});

export default apiClient;
