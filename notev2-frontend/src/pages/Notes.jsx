import { useEffect, useState } from "react";
import { getNotes, createNote, deleteNote} from "../api/notes";
import { getCurrentUser} from "../api/auth";

function Notes() {
  const [notes, setNotes] = useState([]);
  const [newNote, setNewNote] = useState("");
  const [username, setUsername] = useState("");

  useEffect(() => {
    fetchNotes();
    fetchUser();
  }, []);

  const fetchNotes = async () => {
    try {
      const response = await getNotes();
      setNotes(response.data);
    } catch (error) {
      console.error("Fehler beim Laden der Notes", error);
    }
  };

  const fetchUser = async () => {
    try {
      const response = await getCurrentUser();
      setUsername(response.data.message);
    } catch (error) {
      console.error("Fehler beim Laden des Benutzers", error);
    }
  };

  const handleCreateNote = async () => {
    try {
      const response = await createNote({ content: newNote });
      setNewNote("");
      fetchNotes();
    } catch (error) {
      console.error("Fehler beim Erstellen der Notiz", error);
    }
  };

  const handleDeleteNote = async (id) => {
    try {
      await deleteNote(id);
      fetchNotes();
    } catch (error) {
      console.error("Fehler beim Löschen der Notiz", error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };

  /*return (
    <div>
      <h2>Meine Notizen</h2>
        <p>Angemeldet als: {username}</p> 
        <button onClick={handleLogout}>Logout</button>
        <br/><br/>  


        <input
          type="text"
          placeholder="Neue Notiz..."
          value={newNote}
          onChange={(e) => setNewNote(e.target.value)}
        />

        <button onClick={handleCreateNote}>Hinzufügen</button>

        <br /><br />

      {notes.length === 0 ? (
        <p>Keine Notizen vorhanden</p>
      ) : (
        <ul>
          {notes.map((note) => (
            <li key={note.id}>
              {note.content}
              <button onClick={() => handleDeleteNote(note.id)}>Löschen</button>
            </li>
          ))}
        </ul>
      )}

    </div>
  );*/

  return (
  <div className="p-6 bg-gray-100 min-h-screen">

    <div className="flex justify-between mb-4">
      <h2 className="text-2xl font-bold">Meine Notizen</h2>

      <button
        onClick={handleLogout}
        className="bg-red-500 text-white px-4 py-1 rounded hover:bg-red-600"
      >
        Logout
      </button>
    </div>

    <p className="mb-4">Eingeloggt als: {username}</p>

    <div className="flex gap-2 mb-6">
      <input
        type="text"
        placeholder="Neue Notiz..."
        value={newNote}
        onChange={(e) => setNewNote(e.target.value)}
        className="flex-1 p-2 border rounded"
      />
      <button
        onClick={handleCreateNote}
        className="bg-green-500 text-white px-4 rounded hover:bg-green-600"
      >
        +
      </button>
    </div>

    <ul className="space-y-2">
      {notes.map((note) => (
        <li
          key={note.id}
          className="bg-white p-3 rounded shadow flex justify-between"
        >
          <span>{note.content}</span>

          <button
            onClick={() => handleDeleteNote(note.id)}
            className="bg-red-500 text-white px-4 py-1 rounded hover:bg-red-600"
          >
            Löschen
          </button>
        </li>
      ))}
    </ul>
  </div>
);
}

export default Notes;