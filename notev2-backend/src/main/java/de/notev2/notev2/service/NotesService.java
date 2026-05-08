package de.notev2.notev2.service;

import java.util.List;
import org.springframework.stereotype.Service;



import de.notev2.notev2.entity.Note;
import de.notev2.notev2.entity.User;
import de.notev2.notev2.exception.EmptyFieldException;
import de.notev2.notev2.exception.InvalidCredentialsException;
import de.notev2.notev2.exception.ResourceNotFoundException;
import de.notev2.notev2.repos.NoteRepository;
import de.notev2.notev2.repos.UserRepository;

@Service
public class NotesService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NotesService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }
   
    public Note createNote(Note note, String username) {
        if (note.getTitle() == null || note.getTitle().isEmpty()) {
            throw new EmptyFieldException("Titel darf nicht leer sein");
        }
        User user = userRepository.findByUsername(username);
        note.setUser(user);
        return noteRepository.save(note);
    }

    public List<Note> getMyNotes(String username) {
        User user = userRepository.findByUsername(username);
        return noteRepository.findByUserOrderByCreatedAtDesc(user);
    }


    public void deleteNote(Long id, String username) {
        
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notiz nicht gefunden"));


        if (!note.getUser().getUsername().equals(username)) {

            throw new InvalidCredentialsException("Notiz gehört nicht dem Benutzer");

        }

        noteRepository.delete(note);

    }

    public Note updateNote(Long id, Note updatedNote, String username) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notiz nicht gefunden"));

        if (!note.getUser().getUsername().equals(username)) {
            throw new InvalidCredentialsException("Notiz gehört nicht dem Benutzer");
        }

        if (updatedNote.getTitle() == null || updatedNote.getTitle().isEmpty()) {
            throw new EmptyFieldException("Titel darf nicht leer sein");
        }

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        return noteRepository.save(note);
    }
}
