package de.notev2.notev2.dto;

import jakarta.validation.constraints.NotBlank;

public class NoteCreationRequest {

    @NotBlank(message = "Titel darf nicht leer sein")
    private String title;


    private String content;



    public NoteCreationRequest() {
    }

    public NoteCreationRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
