import {request} from "../AxiosConfig";

const baseURL = process.env.REACT_APP_API_BASE_URL;


const apiUrl = `${baseURL}/api/public/products`;
const apiUrlEvent = `${baseURL}/api/public/events`;

//Products API
export const getProducts = async () => {
  try {
    const response = await request('GET','/api/products',false)
    return response.data;
  } catch (error) {
    console.error('Error fetching products:', error.response || error);
    throw error;
  }
};

export const createProduct = async (product) => {
  try {
    const response = await request('POST','/api/products',true)
    return response.data;
  } catch (error) {
    console.error('Error creating product:', error.response || error);
    throw error;
  }
};

export const getProductById = async (id) => {
  try {
    const response = await request('GET',`/api/products/${id}`,false)
    return response.data;
  } catch (error) {
    console.error(`Error fetching product with ID ${id}:`, error.response || error);
    throw error;
  }
};

export const updateProductById = async (id, product) => {
  try {
    const response = await request('PUT',`/api/products/${id}`,product,true)
    return response.data;
  } catch (error) {
    console.error(`Error updating product with ID ${id}:`, error.response || error);
    throw error;
  }
};

export const deleteProductById = async (id) => {
  try {
    const response = await request('DELETE',`/api/products/${id}`,true);
    return response.data;
  } catch (error) {
    console.error(`Error deleting product with ID ${id}:`, error.response || error);
    throw error;
  }
};

// Events API
export const getEvents = async () => {
  try {
    
    const response = await request('GET','/api/public/events',false)
    const events = response.data;

    if (Array.isArray(events)) {
      return events
        .filter(event => event.startTime) // Ensure event has a startTime
        .sort((a, b) => {
          const dateA = new Date(a.startTime.replace(" ", "T")).getTime(); // Convert to ISO format
          const dateB = new Date(b.startTime.replace(" ", "T")).getTime();
          return dateA - dateB; // Ascending order
        });
    } else {
      console.error('Fetched events are not an array:', events);
      return [];
    }
  } catch (error) {
    console.error('Error fetching events:', error.response || error);
    throw error;
  }
};





export const getEventById = async (id) => {
  try {
    const response = await request('GET',`/api/public/events/${id}`,false)
    return response.data;
  } catch (error) {
    console.error(`Error fetching event with ID ${id}:`, error.response || error);
    throw error;
  }
};

export const deleteEventById = async (id) => {
  try {
    const response = await request('DELETE',`/api/events/${id}`,true);
    return response.data;
  } catch (error) {
    console.error(`Error deleting event with ID ${id}:`, error.response || error);
    throw error;
  }
};
