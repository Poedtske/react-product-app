import {jwtDecode} from "jwt-decode"; // Ensure proper import syntax

// Get the authentication token from localStorage
export const getAuthToken = () => {
  return localStorage.getItem("auth_token");
};

// Remove the authentication token from localStorage
export const removeAuthToken = () => {
  localStorage.removeItem("auth_token");
};

// Decode JWT and get user details
export const getUserFromToken = () => {
  const token = getAuthToken(); // Use getAuthToken to retrieve the token
  if (token) {
    try {
      const decoded = jwtDecode(token);
      return decoded; // Returns the decoded JWT payload (e.g., { role: 'USER', ... })
    } catch (error) {
      console.error("Invalid token", error);
      return null;
    }
  }
  return null;
};

export const getUserRole =()=>{
  const user= getUserFromToken();
  return user.role[0].authority;
}

// Check if the user has a specific role
export const hasRole = (role) => {
  const user = getUserFromToken();
  return user && user.role === role;
};

// Check if the user is authenticated
export const isAuthenticated = () => {
  return getUserFromToken() !== null;
};
