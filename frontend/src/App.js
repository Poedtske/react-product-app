import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import NavBar from './components/NavBar';
import Footer from './components/Footer';
import Home from './pages/home/Home';
import Bestuur from './pages/fanfare/bestuur/Bestuur';
import Dirigent from './pages/fanfare/dirigent/Dirigent';
import Geschiedenis from './pages/fanfare/geschiedenis/Geschiedenis';
import Instrumenten from './pages/fanfare/instrumenten/Instrumenten';
import Jeugd from './pages/jeugd/Jeugd';
import Documenten from './pages/info/documenten/Documenten';
import Privacy from './pages/info/privacy/Privacy';
import Kalender from './pages/kalender/Kalender';
import Sponsors from './pages/sponsors/Sponsors';
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
        <Route path="/fanfare/dirigent" element={<Dirigent />} />
        <Route path="/fanfare/geschiedenis" element={<Geschiedenis />} />
        <Route path="/fanfare/instrumenten" element={<Instrumenten />} />
        <Route path="/jeugd" element={<Jeugd />} />
        <Route path="/info/documenten" element={<Documenten />} />
        <Route path="/info/privacy" element={<Privacy />} /> 
        <Route path="/kalender" element={<Kalender />} />
        <Route path="/sponsors" element={<Sponsors />} />
      </Routes>
      <Footer/>
    </>
    
  );
}

export default App;
