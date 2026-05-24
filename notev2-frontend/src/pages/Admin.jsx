import { useNavigate } from "react-router-dom";
import { getAllUsers, registerUser } from "../api/user";
import { getCurrentUser } from "../api/auth";
import { useEffect, useState } from "react";

function Admin() {
  const [users, setUsers] = useState([]);          // ← alle User als Array
  const [currentUser, setCurrentUser] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [admin, setAdmin] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    fetchAllUsers();
    fetchCurrentUser();
  }, []);

  const fetchAllUsers = async () => {
    try {
      const response = await getAllUsers();
      setUsers(response.data);                     // ← Array setzen
    } catch (error) {
      console.error("Fehler beim Laden der Benutzer", error);
    }
  };

  const fetchCurrentUser = async () => {
    try {
      const response = await getCurrentUser();
      setCurrentUser(response.data.message);
    } catch (error) {
      console.error("Fehler beim Laden des Benutzers", error);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    try {
      const response = await registerUser({ username, password, admin });
      setSuccess(response.data.message);
      setUsername("");
      setPassword("");
      setAdmin(false);
      fetchAllUsers();                             // ← Liste neu laden
    } catch (error) {
      setError(error.response.data.message);
    }
  };

  return (
    <div className="p-6 bg-gray-100 min-h-screen">

      {/* Header mit aktuellem User */}
      <div className="flex items-center justify-between mb-6 pb-4 border-b border-gray-200">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-full bg-blue-100 flex items-center justify-center font-medium text-blue-600">
            {currentUser.charAt(0).toUpperCase()}
          </div>
          <div>
            <p className="text-xs text-gray-400">Eingeloggt als</p>
            <p className="font-medium">{currentUser}</p>
          </div>
        </div>
        <span className="text-xs bg-blue-100 text-blue-700 px-3 py-1 rounded-full">Admin</span>
        <button onClick={() => navigate("/notes")} className="text-sm text-gray-500 hover:text-gray-700">
          ← Zurück
        </button>
      </div>

      <div className="grid grid-cols-2 gap-6">

        {/* Links: Alle User */}
        <div className="bg-white rounded-xl shadow p-5">
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-semibold text-lg">Alle Benutzer</h2>
            <span className="text-xs text-gray-400">{users.length} Benutzer</span>
          </div>
          <div className="flex flex-col gap-2">
            {users.map((user) => (
              <div key={user.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 rounded-full bg-gray-200 flex items-center justify-center text-sm font-medium">
                    {user.username.charAt(0).toUpperCase()}
                  </div>
                  <span className="font-medium">{user.username}</span>
                </div>
                <span className={`text-xs px-2 py-1 rounded-full ${
                  user.admin ? "bg-blue-100 text-blue-700" : "bg-gray-100 text-gray-500"
                }`}>
                  {user.admin ? "Admin" : "User"}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Rechts: Registrieren */}
        <div className="bg-white rounded-xl shadow p-5">
          <h2 className="font-semibold text-lg mb-4">Neuen Benutzer anlegen</h2>
          {error && <p className="text-red-500 text-sm mb-3">{error}</p>}
          {success && <p className="text-green-500 text-sm mb-3">{success}</p>}
          <form onSubmit={handleRegister} className="flex flex-col gap-3">
            <input type="text" placeholder="Benutzername" value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="p-2 border rounded-lg" />
            <input type="password" placeholder="Passwort" value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="p-2 border rounded-lg" />
            <label className="flex items-center gap-2 p-3 bg-gray-50 rounded-lg cursor-pointer">
              <input type="checkbox" checked={admin}
                onChange={(e) => setAdmin(e.target.checked)} />
              <span className="text-sm">Admin-Rechte vergeben</span>
            </label>
            <button type="submit" className="bg-blue-500 text-white p-2 rounded-lg hover:bg-blue-600">
              Registrieren
            </button>
          </form>
        </div>

      </div>
    </div>
  );
}
export default Admin;