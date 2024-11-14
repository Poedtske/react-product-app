import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import NavBar from './components/NavBar';
import Footer from './components/Footer';
import Home from './pages/Home';
import Bestuur from './pages/Bestuur';
import Test from './pages/Test';
import ProductList from "./components/ProductList";
import Products from "./components/Products";
import { ProductListProvider } from "./context/ProductContext";
import ProductDetail from "./components/ProductDetail";
import UpdateProductForm from './components/UpdateProductForm';
import CreateProductForm from './components/CreateProductForm'
import './App.css';

function App() {
  return (

    <>
      <NavBar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/fanfare/bestuur" element={<Bestuur />} />
        <Route path="/fanfare/dirigent" element={<Test />} />
        {/* <Route path="/fanfare/geschiedenis" element={<App />} />
        <Route path="/fanfare/instrument" element={<App />} />
        <Route path="/kalender" element={<App />} />
        <Route path="/jeugd" element={<App />} />
        <Route path="/praktischeInfo/documenten" element={<App />} />
        <Route path="/praktischeInfo/privacy" element={<App />} /> */}
      </Routes>
      <Footer/>
    </>
    
  );
}

export default App;
