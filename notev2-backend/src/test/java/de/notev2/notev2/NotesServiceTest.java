package de.notev2.notev2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.notev2.notev2.dto.NoteCreationRequest;
import de.notev2.notev2.dto.NoteResponse;
import de.notev2.notev2.entity.Note;
import de.notev2.notev2.entity.User;
import de.notev2.notev2.exception.InvalidCredentialsException;
import de.notev2.notev2.exception.ResourceNotFoundException;
import de.notev2.notev2.repos.NoteRepository;
import de.notev2.notev2.repos.UserRepository;
import de.notev2.notev2.service.NotesService;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotesService notesService;

    // =========================
    // CREATE NOTE
    // =========================

    @Test
    void shouldCreateNote() {
        User user = new User();
        user.setUsername("deniz");

        NoteCreationRequest dto = new NoteCreationRequest("Test", "Test Content");


        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());

        when(userRepository.findByUsername("deniz")).thenReturn(user);
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        NoteResponse result = notesService.createNote(dto, "deniz");

        assertEquals("Test", result.getTitle());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

   

    // =========================
    // GET MY NOTES
    // =========================

    @Test
    void shouldReturnUserNotes() {
        User user = new User();
        user.setUsername("deniz");

        List<Note> notes = List.of(new Note(), new Note());


        when(userRepository.findByUsername("deniz")).thenReturn(user);
        when(noteRepository.findByUserOrderByIdDesc(user)).thenReturn(notes);

        List<NoteResponse> result = notesService.getMyNotes("deniz");

        assertEquals(2, result.size());
    }


    // =========================
    // DELETE NOTE
    // =========================

    @Test
    void shouldDeleteNote() {
        User user = new User();
        user.setUsername("deniz");
        user.setLongId(1L);
        when(userRepository.findByUsername("deniz")).thenReturn(user);

        Note note = new Note();
        note.setLongId(1L);
        note.setUser(user);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        notesService.deleteNote(1L, "deniz");

        verify(noteRepository, times(1)).delete(note);
    }

    @Test
    void shouldNotDeleteIfNoteNotFound() {

        User user = new User();
        user.setUsername("deniz");
        user.setLongId(1L);

        when(userRepository.findByUsername("deniz")).thenReturn(user);
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());
        

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            notesService.deleteNote(1L, "deniz");
        });

        assertEquals("Notiz nicht gefunden", exception.getMessage());
    }

    @Test
    void shouldNotDeleteIfNotOwner() {
        User owner = new User();
        owner.setUsername("someoneElse");
        owner.setLongId(2L);

        User user = new User();
        user.setUsername("deniz");
        user.setLongId(1L);

        when(userRepository.findByUsername("deniz")).thenReturn(user);

        Note note = new Note();
        note.setUser(owner);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            notesService.deleteNote(1L, "deniz");
        });

        assertEquals("Notiz gehört nicht dem Benutzer", exception.getMessage());
        verify(noteRepository, never()).delete(any());
    }
}