import { useEffect, useState } from "react";
import { getNotes, createNote, deleteNote} from "../api/notes";
import { getCurrentUser} from "../api/auth";

function Notes() {
  const [notes, setNotes] = useState([]);
  const [newNote, setNewNote] = useState("");
  const [newTitle, setNewTitle] = useState("");
  const [username, setUsername] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [noteId, setNoteId] = useState(null);

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
      setError(error.response.data.message);
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
      const response = await createNote({ title: newTitle, content: newNote });
      setNewTitle("");
      setNewNote("");
      setSuccess("Notiz erstellt");
      setError("");
      setTimeout(() => {
        setSuccess("");
      }, 2000);

      fetchNotes();
    } catch (error) {
      console.error(error);
      setError(error.response.data.message);
      setSuccess("");
    }
  };

  const handleDeleteNote = async (id) => {
    try {
      await deleteNote(id);
      fetchNotes();
      setSuccess("Notiz gelöscht");
      setError("");
    } catch (error) {
      console.error(error);
      setError(error.response.data.message);
      setSuccess("");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };

  const activeNote = notes.find(note => note.id === noteId);

  const toggleNote = (id) => {
    setNoteId(noteId === id ? null : id);
  }


  return (
  <div className="p-6 bg-gray-100 min-h-screen">

    <div className="flex justify-between mb-4">
      <h2 className="text-2xl font-bold">Meine Notizen</h2>
      {error && <p className="text-red-500 mb-2">{error}</p>}
      {success && <p className="text-green-500 mb-2">{success}</p>}

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
        type="title"
        placeholder="Titel..."
        value={newTitle}
        onChange={(e) => setNewTitle(e.target.value)}
        className="flex-1 p-2 border rounded"
      />
      <input
        type="text"
        placeholder="Inhalt..."
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
    

    <div className="flex flex-1 gap-6 overflow-hidden">
    <ul className="w-1/3 space-y-2">
      {notes.map((note) => (
        <li
          key={note.id}

          onClick={() => toggleNote(note.id)}
          className={`p-4 rounded shadow cursor-pointer transition-colors ${
                noteId === note.id ? "bg-blue-500 text-white" : "bg-white hover:bg-gray-50"
              }`}
          
        > 
        <div className="flex justify-between items-center">
          <span className="font-semibold truncate">{note.title}</span>
         <button
          onClick={(e) => {
            e.stopPropagation(); // Verhindert, dass beim Löschen die Notiz aufklappt
            handleDeleteNote(note.id);
          }}
          
          className="bg-red-500 text-white px-4 py-1 rounded hover:bg-red-600"
        >
          Löschen
          </button>
        </div>
        </li>
      ))}
    </ul>
    <div className="w-2/3 bg-white p-8 rounded shadow-lg border border-gray-200 min-h-[300px]">
          {activeNote ? (
            <div>
              <h3 className="text-3xl font-bold mb-4 border-b pb-2 text-gray-800 truncate">{activeNote.title}</h3>
              <p className="text-lg text-gray-700 leading-relaxed whitespace-pre-wrap">
                {activeNote.content}
              </p>
            </div>
          ) : (
            <div className="h-full flex items-center justify-center text-gray-400 italic">
              Wähle eine Notiz aus der Liste aus, um den Inhalt zu lesen.
            </div>
          )}
        </div>

    <div className="text-center text-gray-500 mt-4"> test </div>
     </div> 
  </div>
);
}

export default Notes;