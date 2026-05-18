package de.notev2.notev2.service;

import java.util.List;
import org.springframework.stereotype.Service;

import de.notev2.notev2.dto.NoteCreationRequest;
import de.notev2.notev2.dto.NoteResponse;
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

    
    private NoteResponse mapToResponse(Note note) {

        NoteResponse response = new NoteResponse();

        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());

        return response;
    }

    public NoteResponse createNote(NoteCreationRequest dto, String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new InvalidCredentialsException("Benutzer nicht gefunden");
        }

        if(dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new EmptyFieldException("Titel darf nicht leer sein");
        }

        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setUser(user);

        Note saved = noteRepository.save(note);

        return mapToResponse(saved);
    }

    public List<NoteResponse> getMyNotes(String username) {

        User user = userRepository.findByUsername(username); 
        if (user == null) {
            throw new InvalidCredentialsException("Benutzer nicht gefunden");
        }
        return noteRepository.findByUserOrderByIdDesc(user).stream().map(this::mapToResponse).toList();
    }


    public void deleteNote(Long id, String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidCredentialsException("Benutzer nicht gefunden");
        }

        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notiz nicht gefunden"));


        if (!note.getUser().getUsername().equals(username)) {

            throw new InvalidCredentialsException("Notiz gehört nicht dem Benutzer");

        }

        noteRepository.delete(note);

    }

    public NoteResponse updateNote(Long id, NoteCreationRequest updatedNote, String username) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notiz nicht gefunden"));

        if (!note.getUser().getUsername().equals(username)) {
            throw new InvalidCredentialsException("Notiz gehört nicht dem Benutzer");
        }

        if (updatedNote.getTitle() == null || updatedNote.getTitle().isBlank()) {
            throw new EmptyFieldException("Titel darf nicht leer sein");
        }

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        Note saved = noteRepository.save(note);
        return mapToResponse(saved);
    }
}
