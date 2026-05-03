import API from "./api";

export const getNotes = () => API.get("/notes");
export const createNote = (note) => API.post("/notes", note);
export const deleteNote = (id) => API.delete(`/notes/${id}`);