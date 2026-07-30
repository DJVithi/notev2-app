package de.notev2.notev2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthRegisterRequest {
    @NotBlank(message = "Benutzername darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Benutzername muss zwischen 2 und 50 Zeichen lang sein")
    @Pattern(regexp = "^\\S+$", message = "Benutzername darf keine Leerzeichen enthalten")
    private String username;

    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 2, max = 50,  message = "Passwort muss mind. 2 Zeichen haben")
    @Pattern(regexp = "^\\S+$", message = "Passwort darf keine Leerzeichen enthalten")
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
