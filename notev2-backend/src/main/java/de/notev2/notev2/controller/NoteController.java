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
import de.notev2.notev2.service.NotesService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NotesService noteService;

    public NoteController(NotesService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note, Authentication auth) {
        return noteService.createNote(note, auth.getName());
    }

    @GetMapping
    public List<Note> getMyNotes(Authentication auth) {
        return noteService.getMyNotes(auth.getName());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, Authentication auth) {
        noteService.deleteNote(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note updatedNote, Authentication auth) {
        Note result = noteService.updateNote(id, updatedNote, auth.getName());
        return ResponseEntity.ok(result);
    }
}

    

