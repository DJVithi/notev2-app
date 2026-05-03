import {BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useState, useEffect } from "react";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import Notes from "./pages/Notes";
import './index.css';


function App() {
  const [token, setToken] = useState(() => localStorage.getItem("token") || null);

  return (
    <BrowserRouter>
      <Routes>

        <Route path="/login" element={<Login setToken={setToken} />} />

        <Route path="/register" element={<Register />} />


        <Route
          path="/notes"
          element={token ? <Notes /> : <Navigate to="/login" />}
        />

        <Route path="*" element={<Navigate to="/notes" />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;