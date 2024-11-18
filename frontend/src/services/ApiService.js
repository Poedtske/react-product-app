import axios from "axios";

const baseURL = process.env.REACT_APP_API_BASE_URL;


const apiUrl = `${baseURL}/api/public/products`;
const apiUrlEvent = `${baseURL}/api/public/events`;

// Centralized Axios instance with default headers
const apiClient = axios.create({
  baseURL: baseURL,
});

// Products API
export const getProducts = async () => {
  try {
    const response = await apiClient.get('/api/products');
    return response.data;
  } catch (error) {
    console.error('Error fetching products:', error.response || error);
    throw error;
  }
};

export const createProduct = async (product) => {
  try {
    const response = await apiClient.post('/api/products', product);
    return response.data;
  } catch (error) {
    console.error('Error creating product:', error.response || error);
    throw error;
  }
};

export const getProductById = async (id) => {
  try {
    const response = await apiClient.get(`/api/products/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching product with ID ${id}:`, error.response || error);
    throw error;
  }
};

export const updateProductById = async (id, product) => {
  try {
    const response = await apiClient.put(`/api/products/${id}`, product);
    return response.data;
  } catch (error) {
    console.error(`Error updating product with ID ${id}:`, error.response || error);
    throw error;
  }
};

export const deleteProductById = async (id) => {
  try {
    const response = await apiClient.delete(`/api/products/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting product with ID ${id}:`, error.response || error);
    throw error;
  }
};

// Events API
export const getEvents = async () => {
  try {
    const response = await apiClient.get('/api/public/events');
    return response.data;
  } catch (error) {
    console.error('Error fetching events:', error.response || error);
    throw error;
  }
};

export const getEventById = async (id) => {
  try {
    const response = await apiClient.get(`/api/public/events/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching event with ID ${id}:`, error.response || error);
    throw error;
  }
};

export const deleteEventById = async (id) => {
  try {
    const response = await apiClient.delete(`/api/events/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting event with ID ${id}:`, error.response || error);
    throw error;
  }
};
