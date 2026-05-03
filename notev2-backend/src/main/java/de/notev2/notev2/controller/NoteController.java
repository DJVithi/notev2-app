package de.notev2.notev2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.notev2.notev2.entity.Note;
import de.notev2.notev2.entity.User;
import de.notev2.notev2.repos.NoteRepository;
import de.notev2.notev2.repos.UserRepository;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteController(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }


    @PostMapping
    public Note createNote(@RequestBody Note note, Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        note.setUser(user);
        return noteRepository.save(note);
    }

    @GetMapping
    public List<Note> getMyNotes(Authentication auth) {
        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        return noteRepository.findByUser(user);
    }
    
    @GetMapping("/user/{userId}")
    public List<Note> getAll(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return noteRepository.findByUser(user);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, Authentication auth) {
        String username = auth.getName();
        Note note = noteRepository.findById(id).orElse(null);

        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        if (!note.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(403).body("Notiz gehört nicht dem Benutzer");
        }

        noteRepository.delete(note);

        return ResponseEntity.ok().build();
    }

    
}
