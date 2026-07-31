package de.notev2.notev2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthLoginRequest {

    @NotBlank (message = "Benutzername darf nicht leer sein")
    @Size(max = 50, message = "Benutzername zu lang")
    private String username;

    @NotBlank (message = "Passwort darf nicht leer sein")
    @Size(max = 50, message = "Passwort zu lang")
    private String password;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
