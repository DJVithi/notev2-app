package de.notev2.notev2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthResponse {
    
    @Schema(description = "JWT-Token für die Authentifizierung",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY4ODQyODAwMCwiZXhwIjoxNjg4NDMyNjAwfQ.abc123def456ghi789jkl012mno345pqr678stu901vwx234yz567890")
    private String token;
    @Schema(description = "Nachricht zur Darstellung im Frontend",
            example = "Login erfolgreich"
    )
    private String message;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
