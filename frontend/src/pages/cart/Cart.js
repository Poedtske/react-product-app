import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { removeProductFromCart, removeTicketFromCart, clearCart, getCart } from "../../services/ApiService"; // Import API functions
import styles from "./Cart.module.css";

const Cart = () => {
  const navigate = useNavigate();
  const [cart, setCart] = useState({
    products: [], // List of products in the cart
    tickets: [],  // List of tickets in the cart
  });
  const [loading, setLoading] = useState(true); // State for loading
  const [error, setError] = useState(null); // State for error handling

  useEffect(() => {
    // Fetch cart data (products and tickets) on component mount
    const fetchCartData = async () => {
      try {
        const cartData = await getCart(); // Replace with actual function to fetch cart data
        setCart(cartData); // Update cart state
        setLoading(false);
      } catch (err) {
        console.error("Error fetching cart data:", err);
        setError("Failed to load cart data. Please try again later.");
        setLoading(false);
      }
    };

    fetchCartData();
  }, []);

  const handleRemoveProduct = async (productId) => {
    try {
      await removeProductFromCart(productId); // Call the API to remove product
      setCart((prevCart) => ({
        ...prevCart,
        products: prevCart.products.filter((product) => product.id !== productId),
      }));
    } catch (err) {
      console.error("Error removing product:", err);
      setError("Failed to remove product. Please try again.");
    }
  };

  const handleRemoveTicket = async (ticketId) => {
    try {
      await removeTicketFromCart(ticketId); // Call the API to remove ticket
      setCart((prevCart) => ({
        ...prevCart,
        tickets: prevCart.tickets.filter((ticket) => ticket.id !== ticketId),
      }));
    } catch (err) {
      console.error("Error removing ticket:", err);
      setError("Failed to remove ticket. Please try again.");
    }
  };

  const handleClearCart = async () => {
    try {
      await clearCart(); // Call the API to clear the entire cart
      setCart({ products: [], tickets: [] }); // Clear cart state locally
    } catch (err) {
      console.error("Error clearing cart:", err);
      setError("Failed to clear cart. Please try again.");
    }
  };

  const handleNavigateToInvoice = () => {
    navigate("/invoice"); // Navigate to the invoice page
  };

  // Calculate the total price of all products and tickets in the cart
  const calculateTotalPrice = () => {
    let total = 0;

    if(cart.products !=null&& cart.products.length!=0){
      for (let product of cart.products) {
        total += product.price;
      }
    }

    if(cart.tickets !=null&& cart.tickets.length!=0){
      for (let ticket of cart.tickets) {
        total += ticket.price;
      }
    }

    return total;
  };

  const totalPrice = calculateTotalPrice();

  if (loading) {
    return <p>Loading your cart...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <main>
      <div className={styles.cart_page}>
        {/* Buttons to navigate or clear the cart */}
        <div className={styles.cart_buttons}>
          <button className={styles.pay_button} onClick={handleNavigateToInvoice}>
            Betalen
          </button>
          <button className={styles.clear_button} onClick={handleClearCart}>
            Clear Cart
          </button>
        </div>

        <h2>Your Cart</h2>

        {/* Display Products in the Cart */}
        {cart.products && cart.products.length > 0 && (
          <div className={styles.cart_section}>
            <h3>Products</h3>
            <ul>
              {cart.products.map((product) => (
                <li key={product.id} className={styles.cart_item}>
                  <span>{product.name}</span> - €{product.price.toFixed(2)}
                  <button
                    className={styles.remove_button}
                    onClick={() => handleRemoveProduct(product.id)}
                  >
                    Remove
                  </button>
                </li>
              ))}
            </ul>
          </div>
        )}

        {/* Display Tickets in the Cart */}
        {cart.tickets && cart.tickets.length > 0 && (
          <div className={styles.cart_section}>
            <h3>Tickets</h3>
            <ul>
              {cart.tickets.map((ticket) => (
                <li key={ticket.id} className={styles.cart_item}>
                  <span>{ticket.name}</span> - €{ticket.price.toFixed(2)}
                  <button
                    className={styles.remove_button}
                    onClick={() => handleRemoveTicket(ticket.id)}
                  >
                    Remove
                  </button>
                </li>
              ))}
            </ul>
          </div>
        )}

        {/* If cart is empty */}
        {cart.products && cart.products.length === 0 && cart.tickets && cart.tickets.length === 0 && (
          <p>Your cart is empty</p>
        )}

        {/* Display the total price */}
        <div className={styles.total_price}>
          <h3>Total: €{totalPrice.toFixed(2)}</h3>
        </div>
      </div>
    </main>
  );
};

export default Cart;
