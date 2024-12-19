import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  getProductById,
  getProductImg,
  addProductToCart,
} from "../services/ApiService";
import {
  Container,
  Typography,
  Box,
  TextField,
  Button,
} from "@mui/material";

const ProductDetails = () => {
  const { id } = useParams(); // Product ID from the route
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [quantity, setQuantity] = useState(1); // State for quantity input
  const [cartOptionsVisible, setCartOptionsVisible] = useState(false); // Show cart options after adding to cart

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const productData = await getProductById(id); // Fetch the product details
        const imageResponse = await getProductImg(id); // Fetch product image

        const productWithImage = {
          ...productData,
          img: URL.createObjectURL(new Blob([imageResponse])), // Convert image response to blob
        };

        setProduct(productWithImage);
        setLoading(false);
      } catch (err) {
        setError("Error fetching product details");
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  const handleAddToCart = async (event) => {
    event.preventDefault();

    try {
      const productToAdd = {
        ...product,
        quantity, // Include the quantity from the input field
      };

      const response = await addProductToCart(productToAdd); // API call to add the product to the cart

      if (response.status === 200) {
        setCartOptionsVisible(true); // Show cart options on success
      }
    } catch (err) {
      if (err.response?.status === 403) {
        navigate("/login");
      }
      console.error("Failed to add product to cart", err);
    }
  };

  if (loading) return <p>Loading product details...</p>;
  if (error) return <p>{error}</p>;

  return (
    <Container maxWidth="md" sx={{ marginTop: 4 }}>
      {/* Go Back Button */}
      <Button
        onClick={() => navigate("/products")}
        sx={{
          mb: 2,
          color: "white",
          backgroundColor: "#007bff",
          "&:hover": {
            backgroundColor: "#0056b3",
          },
        }}
      >
        &larr; Go Back
      </Button>

      {/* Product Details */}
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
        <Typography variant="h4" gutterBottom>
          Product Details
        </Typography>
      </Box>

      {/* Product Image */}
      {product.img && (
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            mb: 3,
          }}
        >
          <img
            src={product.img}
            alt={product.name}
            style={{
              width: "300px",
              height: "300px",
              objectFit: "cover",
              borderRadius: "10px",
            }}
          />
        </Box>
      )}

      {/* Product Information */}
      <Box sx={{ mb: 3 }}>
        <Typography variant="h6">Product Information</Typography>
        <Typography variant="body1">
          <strong>Name:</strong> {product.name}
        </Typography>
        <Typography variant="body1">
          <strong>Price (€):</strong> {product.price}
        </Typography>
        <Typography variant="body1">
          <strong>Available:</strong> {product.available ? "Yes" : "No"}
        </Typography>
        <Typography variant="body1">
          <strong>Category:</strong> {product.category}
        </Typography>
      </Box>

      {/* Add to Cart Form */}
      <Box
        component="form"
        onSubmit={handleAddToCart}
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: 2,
          maxWidth: "400px",
          margin: "0 auto",
          color: "white;",
        }}
      >
        <TextField
          label="Quantity"
          type="number"
          value={quantity}
          onChange={(e) => setQuantity(Number(e.target.value))}
          inputProps={{ min: 1 }}
          required
          InputLabelProps={{
            style: { color: "white" },
          }}
          InputProps={{
            style: { color: "white", borderColor: "white" },
          }}
          sx={{
            "& .MuiOutlinedInput-root": {
              "& fieldset": {
                borderColor: "white",
              },
              "&:hover fieldset": {
                borderColor: "white",
              },
              "&.Mui-focused fieldset": {
                borderColor: "white",
              },
            },
          }}
        />
        <Button
          variant="contained"
          type="submit"
          sx={{
            backgroundColor: "#28a745",
            color: "white",
            "&:hover": {
              backgroundColor: "#218838",
            },
          }}
        >
          Add to Cart
        </Button>
      </Box>

      {/* Cart Options */}
      {cartOptionsVisible && (
        <Box
          sx={{
            marginTop: 4,
            display: "flex",
            flexDirection: "column",
            gap: 2,
            textAlign: "center",
          }}
        >
          <Typography variant="body1">
            Product added to cart successfully!
          </Typography>
          <Box sx={{ display: "flex", justifyContent: "center", gap: 2 }}>
            <Button
              onClick={() => navigate("/products")}
              variant="outlined"
              sx={{
                borderColor: "#007bff",
                color: "#007bff",
                "&:hover": {
                  borderColor: "#0056b3",
                  backgroundColor: "rgba(0, 123, 255, 0.1)",
                },
              }}
            >
              Continue Shopping
            </Button>
            <Button
              onClick={() => navigate("/cart")}
              variant="contained"
              sx={{
                backgroundColor: "#007bff",
                color: "white",
                "&:hover": {
                  backgroundColor: "#0056b3",
                },
              }}
            >
              Go to Cart
            </Button>
          </Box>
        </Box>
      )}
    </Container>
  );
};

export default ProductDetails;
