import axios from "axios";
import { getAuthToken, setAuthToken, removeAuthToken } from "./utils/jwtUtils"; // Import JWT utility functions

// Set default Axios configuration
axios.defaults.baseURL = "http://localhost:8080";
axios.defaults.headers["Content-Type"] = "application/json";

// Make a request using Axios with enhanced options
export const request = (
  method,
  url,
  data,
  headersEnabled,
  deleteContentType = false,
  responseTypeToBlob = false
) => {
  let headers = {};

  if (headersEnabled) {
    const token = getAuthToken(); // Retrieve the token using jwtUtils
    if (token && token !== "ERROR") {
      headers = {
        ...headers, // Retain existing headers
        Authorization: `Bearer ${token}`,
      };
    }
  }

  if (deleteContentType) {
    headers = {
      ...headers, // Retain existing headers
      "Content-Type": "", // Allow Axios to set the correct boundaries automatically
    };
  }

  return axios({
    method: method,
    headers: headers,
    url: url,
    data: data,
    ...(responseTypeToBlob && { responseType: "blob" }), // Add responseType: blob conditionally
  });
};
