import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useState } from "react";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import Notes from "./pages/Notes";
import Admin from "./pages/Admin.jsx";
import { isAdmin, isLoggedIn } from "./utils/auth";
import './index.css';

// Schützt Route: nur eingeloggte User
const PrivateRoute = ({ children }) =>
  isLoggedIn() ? children : <Navigate to="/login" />;

// Schützt Route: nur Admins
const AdminRoute = ({ children }) => {
  if (!isLoggedIn()) return <Navigate to="/login" />;
  if (!isAdmin()) return <Navigate to="/notes" />; 
  return children;
};

function App() {
  const [token, setToken] = useState(() => localStorage.getItem("token") || null);
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login setToken={setToken} />} />
        <Route path="/register" element={<Register />} />

        <Route path="/notes" element={
          <PrivateRoute><Notes /></PrivateRoute>
        } />

        <Route path="/user" element={
          <AdminRoute><Admin /></AdminRoute>
        } />

        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;