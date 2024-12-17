import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCart, pay } from "../../services/ApiService"; // Import the cart fetching function and pay function
import styles from "./Invoice.module.css"; // Import CSS module for styling

const Invoice = () => {
  const navigate = useNavigate();

  const [cart, setCart] = useState({
    products: [],
    tickets: [],
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCartData = async () => {
      try {
        const cartData = await getCart(); // Fetch cart data
        setCart(cartData);
        setLoading(false);
      } catch (err) {
        console.error("Error fetching cart data:", err);
        setError("Failed to load cart data. Please try again later.");
        setLoading(false);
      }
    };

    fetchCartData();
  }, []);

  // Function to calculate total price of cart items
  const calculateTotalPrice = () => {
    let total = 0;

    if (cart.products != null && cart.products.length !== 0) {
      for (let product of cart.products) {
        total += product.price;
      }
    }

    if (cart.tickets != null && cart.tickets.length !== 0) {
      for (let ticket of cart.tickets) {
        total += ticket.price;
      }
    }

    return total;
  };

  const handlePay = async () => {
    try {
      await pay(); // Call the pay function from the APIService
      alert("Payment successful!"); // Optional: Show confirmation to the user
      navigate("/"); // Navigate back to home after successful payment
    } catch (err) {
      console.error("Payment failed:", err);
      alert("Payment failed. Please try again.");
    }
  };

  const totalPrice = calculateTotalPrice();

  if (loading) {
    return <p>Loading your invoice...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <main className={styles.invoice_page}>
      <div className={styles.invoice_content}>
        {/* Display Total Amount */}
        <h1>Invoice</h1>
        <p className={styles.total_amount}>
          <strong>Total Amount: €{totalPrice.toFixed(2)}</strong>
        </p>

        {/* Big Image */}
        <div className={styles.image_container}>
          <img src="/images/payconnicQrcode.png" alt="Invoice" className={styles.invoice_image} />
        </div>

        {/* Paragraphs */}
        <div className={styles.invoice_text}>
          <p>
            Rekening nummer: <b>12544 454 751244 51044</b>
          </p>
          <p>
            Gelieve u email in de beschrijving van u transactie te zetten. Druk op de betaal knop wanneer u betaald heeft, hierna zullen wij u een mail versturen om de betaling te bevestigen.
          </p>
          <p>
            Indien er vragen zijn omtrent u order, contacteer ons via <b>k.f.demoedigevrienden@gmail.com</b>.
          </p>
        </div>

        {/* Button to Handle Payment */}
        <div className={styles.button_container}>
          <button className={styles.pay_button} onClick={handlePay}>
            Ik heb Betaald
          </button>
        </div>
      </div>
    </main>
  );
};

export default Invoice;
