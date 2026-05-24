import { useNavigate } from "react-router-dom";
import { useState } from "react";
import API from "../api/api";

function Register()  {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  
    const handleRegister = async (e) => {
        e.preventDefault();
        setError("");
        setSuccess("");
        try {
            const response = await API.post("/auth/register", { username, password });
            console.log("Registration erfolgreich:", response.data.message);
            setSuccess(response.data.message);
            navigate("/login");

        } catch (error) {
            console.error("Registration fehlgeschlagen:", error);
            setError(error.response.data.message);
            
        }
        setUsername("");
        setPassword("");
    };  
    

    return (
  <div className="flex items-center justify-center h-screen bg-gray-100">
    <form
      onSubmit={handleRegister}
      className="bg-white p-8 rounded-2xl shadow-md w-80"
    >
      <h2 className="text-2xl font-bold mb-6 text-center">Registrieren</h2>

      {error && <p className="text-red-500 mb-2 text-center">{error}</p>}
      {success && <p className="text-green-500 mb-2 text-center">{success}</p>}

      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        className="w-full mb-3 p-2 border rounded"
      />

      <input
        type="password"
        placeholder="Passwort"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="w-full mb-4 p-2 border rounded"
      />
      <div className="flex gap-4 mt-2"> 
        <button
          type="submit"
          className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600"
        > Registrieren
        </button>
      </div>
    </form>
  </div>
);
}
export default Register;