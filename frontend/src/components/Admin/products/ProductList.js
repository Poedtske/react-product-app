import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getProducts } from "../../../services/ApiService"; // Replace with the correct API call
import styles from "../../../pages/kalender/kalender.module.css"; // Import CSS module

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const productData = await getProducts(); // Fetch the products list
        console.log("Fetched Products:", productData);
        setProducts(productData);
        setLoading(false);
      } catch (err) {
        setError("Error fetching products");
        setLoading(false);
        console.error("Error fetching products:", err);
      }
    };
    fetchProducts();
  }, []);

  if (loading) return <p>Loading products...</p>;
  if (error) return <p>{error}</p>;

  return (
    <main className={styles.main}>
      <h2 className={styles.title}>Product List</h2>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Price (€)</th>
            <th>Image</th>
            <th>Available</th>
            <th>Category</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
              <td>{product.price}</td>
              <td>
                <img
                  src={product.img}
                  alt={product.name}
                  style={{ width: "50px", height: "50px", objectFit: "cover" }}
                />
              </td>
              <td>{product.available ? "Yes" : "No"}</td>
              <td>{product.category?.name || "N/A"}</td>
              <td>
                <Link
                  to={`/admin/products/${product.id}`}
                  className={styles.editLink}
                >
                  View
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </main>
  );
};

export default ProductList;
