import React, { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import {
  getProductById,
  deleteProductById,
  updateProductById,
} from "../../../services/ApiService"; // Replace with correct paths
import styles from "../../../pages/kalender/kalender.module.css"; // CSS module
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
  TextField,
} from "@mui/material";

const ProductDetails = () => {
  const { id } = useParams(); // Product ID from the route
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [updates, setUpdates] = useState({}); // State for updating product fields

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const productData = await getProductById(id);
        console.log("Product Details:", productData);
        setProduct(productData);
        setUpdates({
          name: productData.name,
          price: productData.price,
          available: productData.available,
        });
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

  const handleUpdate = async () => {
    try {
      await updateProductById(id, updates);
      alert("Product updated successfully");
    } catch (err) {
      console.error("Failed to update product", err);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUpdates({ ...updates, [name]: value });
  };

  if (loading) return <p>Loading product details...</p>;
  if (error) return <p>{error}</p>;

  return (
    <Container maxWidth="md" className={styles.main}>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
        <Typography variant="h4" gutterBottom>
          Product Details
        </Typography>
        <Button
          variant="contained"
          color="primary"
          component={Link}
          to="/admin/products/create"
        >
          Create Product
        </Button>
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

      <Box sx={{ mb: 3 }}>
        <Typography variant="h6">Product Information</Typography>
        <TextField
          label="Name"
          name="name"
          value={updates.name}
          onChange={handleInputChange}
          fullWidth
          margin="normal"
        />
        <TextField
          label="Price (€)"
          name="price"
          value={updates.price}
          onChange={handleInputChange}
          type="number"
          fullWidth
          margin="normal"
        />
        <TextField
          label="Available"
          name="available"
          value={updates.available}
          onChange={handleInputChange}
          fullWidth
          margin="normal"
        />
      </Box>

      {/* Actions */}
      <Box sx={{ display: "flex", justifyContent: "center", gap: 2, mb: 4 }}>
        <Button variant="contained" color="primary" onClick={handleUpdate}>
          Update Product
        </Button>
        <Button variant="outlined" color="error" onClick={handleDelete}>
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
                  <TableCell>
                    <TextField
                      defaultValue={invoice.paid ? "Yes" : "No"}
                      InputProps={{ readOnly: true }}
                    />
                  </TableCell>
                  <TableCell>
                    <TextField
                      defaultValue={invoice.confirmed ? "Yes" : "No"}
                      InputProps={{ readOnly: true }}
                    />
                  </TableCell>
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
