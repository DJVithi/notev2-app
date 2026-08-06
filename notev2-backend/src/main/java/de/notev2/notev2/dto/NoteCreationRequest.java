package de.notev2.notev2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NoteCreationRequest {

    @NotBlank(message = "Titel darf nicht leer sein")
    @Size(max = 50, message = "Titel darf nicht länger als 50 Zeichen sein")
    private String title;


    private String content;


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
