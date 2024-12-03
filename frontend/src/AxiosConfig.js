import axios from "axios";


const baseURL=process.env.REACT_APP_BASE_URL
// Create an Axios instance with default headers
const axiosInstance = axios.create({
  baseURL: "http://localhost:8080", // Ensure this is defined in your environment variables
  headers: {
    "Content-Type": "application/json",
  },
});

// Add an interceptor to include authentication
axiosInstance.interceptors.request.use(
  (config) => {
    // Load username and password from environment variables
    const username = process.env.REACT_APP_API_USERNAME;
    const password = process.env.REACT_APP_API_PASSWORD;

    // Add auth to config if credentials are available
    if (username && password) {
      config.auth = {
        username,
        password,
      };
    }

    return config; // Ensure config is returned
  },
  (error) => {
    return Promise.reject(error); // Handle request errors
  }
);

export default axiosInstance;
