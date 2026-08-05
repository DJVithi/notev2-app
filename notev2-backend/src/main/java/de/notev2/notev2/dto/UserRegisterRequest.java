package de.notev2.notev2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterRequest {

    @NotBlank(message = "Benutzername darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Benutzername muss zwischen 2 und max 50 Zeichen lang sein")
    @Pattern(regexp = "^\\S+$", message = "Benutzername darf keine Leerzeichen enthalten")
    private String username;

    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 2, max = 50, message = "Passwort muss zwischen 2 und max 50 Zeichen lang sein")
    @Pattern(regexp = "^\\S+$", message = "Passwort darf keine Leerzeichen enthalten")
    private String password;

    private boolean isAdmin;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}
