import axios from "axios";

// Create an Axios instance with default headers
// const axiosInstance = axios.create({
//   baseURL: "http://localhost:8080", // Ensure this is defined in your environment variables
//   headers: {
//     "Content-Type": "application/json",
//   },
// });

// Add an interceptor to include authentication
// axiosInstance.interceptors.request.use(
//   (config) => {
//     // Load username and password from environment variables
//     const username = process.env.REACT_APP_API_USERNAME;
//     const password = process.env.REACT_APP_API_PASSWORD;

//     // Add auth to config if credentials are available
//     if (username && password) {
//       config.auth = {
//         username,
//         password,
//       };
//     }

//     return config; // Ensure config is returned
//   },
//   (error) => {
//     return Promise.reject(error); // Handle request errors
//   }
// );

// export default axiosInstance;

axios.defaults.baseURL="http://localhost:8080";
axios.defaults.headers["Content-type"]='application/json';

export const getAuthToken=()=>{
    if(window.localStorage.getItem("auth_token")!=null){
        return window.localStorage.getItem("auth_token");
    }else{
        return "ERROR"
    }
    
}

export const setAuthToken=(token)=>{
    window.localStorage.setItem("auth_token", token);
}

export const removeAuthToken = () => {
    window.localStorage.removeItem("auth_token");
  };

export const request=(method,url,data,headersEnabled,deleteContentType=false,responseTypeToBlob=false)=>{
    let headers= {};
    
    if (headersEnabled) {
        if (getAuthToken() != null && getAuthToken() !== "null") {
            headers = {
                ...headers, // Retain existing headers
                "Authorization": `Bearer ${getAuthToken()}`,
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


    // axiosInstance.interceptors.request.use(
    //     (config) => {
    //       // Load username and password from environment variables
    //       const username = process.env.REACT_APP_API_USERNAME;
    //       const password = process.env.REACT_APP_API_PASSWORD;
      
    //       // Add auth to config if credentials are available
    //       if (username && password) {
    //         config.auth = {
    //           username,
    //           password,
    //         };
    //       }
      
    //       return config; // Ensure config is returned
    //     },
    //     (error) => {
    //       return Promise.reject(error); // Handle request errors
    //     }
    //   );

    //   return axiosInstance;
}
