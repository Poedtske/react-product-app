import React, { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import {
  adminGetProductById,
  getProductImg,
  deleteProductById,
} from "../../../services/ApiService"; // Replace with correct paths
import styles from "./ProductDetails.module.css"; // CSS module
import {
  Container,
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
} from "@mui/material";

const ProductDetails = () => {
  const { id } = useParams(); // Product ID from the route
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const productData = await adminGetProductById(id); // Fetch the product details
        const imageResponse = await getProductImg(id); // Fetch product image
        
        // Calculate the quantity based on the sum of the invoice amounts
        const totalQuantity = productData.invoices
          ? productData.invoices.reduce((sum, invoice) => sum + invoice.amount, 0)
          : 0;

        const productWithImageAndQuantity = {
          ...productData,
          img: URL.createObjectURL(new Blob([imageResponse])), // Convert image response to blob
          quantity: totalQuantity, // Set quantity as the sum of invoice amounts
        };

        setProduct(productWithImageAndQuantity);
        setLoading(false);
      } catch (err) {
        setError("Error fetching product details");
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  const handleDelete = async () => {
    try {
      await deleteProductById(id);
      alert("Product deleted successfully");
      navigate("/admin/products"); // Redirect to product list
    } catch (err) {
      console.error("Failed to delete product", err);
    }
  };

  if (loading) return <p>Loading product details...</p>;
  if (error) return <p>{error}</p>;

  return (
    <Container maxWidth="md" className={styles.main}>
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
            src={product.img} // Use the image URL created from the Blob
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
        <Typography variant="body1"><strong>Name:</strong> {product.name}</Typography>
        <Typography variant="body1"><strong>Price (€):</strong> {product.price}</Typography>
        <Typography variant="body1"><strong>Available:</strong> {product.available ? "Yes" : "No"}</Typography>
        <Typography variant="body1"><strong>Quantity:</strong> {product.quantity}</Typography>
        <Typography variant="body1"><strong>Category:</strong> {product.category}</Typography>
      </Box>

      {/* Actions */}
      <Box sx={{ display: "flex", justifyContent: "center", gap: 2, mb: 4 }}>
        {/* Navigate to edit page */}
        <Button
          variant="contained"
          component={Link}
          to={`/admin/products/update/${id}`} // Link to the edit page
          sx={{
            backgroundColor: "#fa6908", // Background color for the Edit button
            color: "white", // Text color
            "&:hover": {
              backgroundColor: "#e15d05", // Hover background color
            },
          }}
        >
          Edit Product
        </Button>

        {/* Delete button */}
        <Button
          variant="contained" // Use 'contained' for solid background
          onClick={handleDelete}
          sx={{
            backgroundColor: "#dc3545", // Initial background color for the Delete button
            color: "white", // Text color for the Delete button
            "&:hover": {
              backgroundColor: "#c82333", // Darker red background color on hover
              borderColor: "#c82333", // Darker red border color on hover
            },
            borderColor: "#dc3545", // Initial border color for the Delete button
          }}
        >
          Delete Product
        </Button>
      </Box>

      {/* Invoice Table */}
      <Typography variant="h6" gutterBottom>
        Invoices Containing this Product
      </Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Invoice ID</TableCell>
              <TableCell>User</TableCell>
              <TableCell>Amount</TableCell>
              <TableCell>Paid</TableCell>
              <TableCell>Confirmed</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {product.invoices && product.invoices.length > 0 ? (
              product.invoices.map((invoice) => (
                <TableRow key={invoice.id}>
                  <TableCell>{invoice.id}</TableCell>
                  <TableCell>
                    {invoice.user?.name || "N/A"} <br />
                    {invoice.user?.email || "N/A"}
                  </TableCell>
                  <TableCell>{invoice.amount}</TableCell>
                  <TableCell>{invoice.paid ? "Yes" : "No"}</TableCell>
                  <TableCell>{invoice.confirmed ? "Yes" : "No"}</TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={5} align="center">
                  No invoices found for this product.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  );
};

export default ProductDetails;
