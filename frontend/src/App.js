import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import NavBar from './components/NavBar';
import AdminNavBar from './components/Admin/AdminNavBar';
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
import ShowEvent from './components/ShowEvent';
import Test from './pages/Test';
import ProductList from "./components/ProductList";
import Products from "./components/Products";
import { ProductListProvider } from "./context/ProductContext";
import ProductDetail from "./components/ProductDetail";
import UpdateProductForm from './components/UpdateProductForm';
import CreateProductForm from './components/CreateProductForm'
import './App.css';

import { EventProvider } from './context/EventContext';
import Registration from './components/Registration';
import RegistrationSuccess from './components/RegistrationSuccess';
import Login from './components/Login';
import LoginSuccessful from './components/LoginSuccessful';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider,useAuth } from './context/AuthContext';
import EventsList from './components/Admin/EventsList';
import CreateEvent from './components/Admin/CreateEvent';
import EventDetails from './components/Admin/EventDetails';
import EditEvent from './components/Admin/EditEvent';
import { getUserRole } from './utils/jwtUtils';

function App() {

  const ADMIN_ROLE="ADMIN";
  const adminRoutes = [
    { path: '/admin/events', element: <Kalender /> },
    { path: '/admin/products', element: <Products /> },
    // Add other protected admin routes here
  ];

  if (getUserRole()==ADMIN_ROLE){
    return (

      <>      
          
          
          <AuthProvider>
            <AdminNavBar />
            <EventProvider>
              <Routes>
                <Route path="/" element={<Home />} /> 
                <Route path="/events/:id" element={<ShowEvent/>} />
                <Route path="/admin/events/create" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <CreateEvent/>
              </ProtectedRoute>} />
              <Route path="/admin/events/:id" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <EventDetails/>
              </ProtectedRoute>} />
              <Route path="/admin/events/edit/:id" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <EditEvent/>
              </ProtectedRoute>} />
              </Routes>
            </EventProvider>  
            <Routes>         
              <Route path="/fanfare/bestuur" element={<Bestuur />} />
              <Route path="/fanfare/dirigent" element={<Dirigent />} />
              <Route path="/fanfare/geschiedenis" element={<Geschiedenis />} />
              <Route path="/fanfare/instrumenten" element={<Instrumenten />} />
              <Route path="/jeugd" element={<Jeugd />} />
              <Route path="/info/documenten" element={<Documenten />} />
              <Route path="/info/privacy" element={<Privacy />} /> 
              <Route path="/kalender" element={<Kalender />} />
              <Route path="/sponsors" element={<Sponsors />} />
              <Route path="/registration" element={<Registration />} />
              <Route path="/registrationSuccessful" element={<RegistrationSuccess />} />
              <Route path="/login" element={<Login />} />
              <Route path="/loginSuccessful" element={<LoginSuccessful />} />
              
              <Route path="/admin/events" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <EventsList/>
              </ProtectedRoute>} />
            </Routes>
          </AuthProvider>      
          
          <Footer/>
        
      </>
      
    );
  }
  else{
    return (

      <>      
          
          
          <AuthProvider>
            <NavBar />
            <EventProvider>
              <Routes>
                <Route path="/" element={<Home />} /> 
                <Route path="/events/:id" element={<ShowEvent/>} />
                <Route path="/admin/events/create" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <CreateEvent/>
              </ProtectedRoute>} />
              <Route path="/admin/events/:id" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <EventDetails/>
              </ProtectedRoute>} />
              <Route path="/admin/events/edit/:id" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <EditEvent/>
              </ProtectedRoute>} />
              </Routes>
            </EventProvider>  
            <Routes>         
              <Route path="/fanfare/bestuur" element={<Bestuur />} />
              <Route path="/fanfare/dirigent" element={<Dirigent />} />
              <Route path="/fanfare/geschiedenis" element={<Geschiedenis />} />
              <Route path="/fanfare/instrumenten" element={<Instrumenten />} />
              <Route path="/jeugd" element={<Jeugd />} />
              <Route path="/info/documenten" element={<Documenten />} />
              <Route path="/info/privacy" element={<Privacy />} /> 
              <Route path="/kalender" element={<Kalender />} />
              <Route path="/sponsors" element={<Sponsors />} />
              <Route path="/registration" element={<Registration />} />
              <Route path="/registrationSuccessful" element={<RegistrationSuccess />} />
              <Route path="/login" element={<Login />} />
              <Route path="/loginSuccessful" element={<LoginSuccessful />} />
              
              <Route path="/admin/events" element={<ProtectedRoute requiredRole={ADMIN_ROLE}>
                <EventsList/>
              </ProtectedRoute>} />
            </Routes>
          </AuthProvider>      
          
          <Footer/>
        
      </>
      
    );
  }
  
}

export default App;
