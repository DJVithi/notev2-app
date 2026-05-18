package de.notev2.notev2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.notev2.notev2.dto.NoteCreationRequest;
import de.notev2.notev2.dto.NoteResponse;
import de.notev2.notev2.service.NotesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/notes")
@Validated
public class NoteController {

    private final NotesService noteService;

    public NoteController(NotesService noteService) {
        this.noteService = noteService;
    }

    @Operation(summary = "Create Note", description = "Erstellt eine neue Notiz für den angemeldeten Benutzer")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Notiz erfolgreich erstellt"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Fehlende oder ungültige Felder"
        )
    })
    @PostMapping
    public NoteResponse createNote(@Valid @RequestBody NoteCreationRequest note, Authentication auth) {

        return noteService.createNote(note, auth.getName());
    }

    @Operation(summary = "Get My Notes", description = "Ruft alle Notizen des angemeldeten Benutzers ab")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Notizen erfolgreich abgerufen"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Nicht authentifiziert"
        )
    })
    @GetMapping
    public List<NoteResponse> getMyNotes(Authentication auth) {
        return noteService.getMyNotes(auth.getName());
    }


    @Operation(summary = "Delete Note", description = "Löscht eine Notiz des angemeldeten Benutzers")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Notiz erfolgreich gelöscht"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Nicht authentifiziert"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Notiz nicht gefunden"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, Authentication auth) {
        noteService.deleteNote(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update Note", description = "Aktualisiert eine Notiz des angemeldeten Benutzers")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Notiz erfolgreich aktualisiert"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Nicht authentifiziert"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Notiz nicht gefunden"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id, @Valid @RequestBody NoteCreationRequest updatedNote, Authentication auth) {
        NoteResponse result = noteService.updateNote(id, updatedNote, auth.getName());
        return ResponseEntity.ok(result);
    }
}

    

