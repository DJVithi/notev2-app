package de.notev2.notev2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.notev2.notev2.entity.User;
import de.notev2.notev2.service.AuthService;
import de.notev2.notev2.dto.AuthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;





@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Register", description = "Registriert einen neuen Benutzer")
        @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registrierung erfolgreich"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Fehlende oder ungültige Felder"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Benutzername bereits vergeben"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        String message = authService.register(user);
        return ResponseEntity.ok(new AuthResponse(null, message));
        
    }

    @Operation(summary = "Login", description = "Authentifiziert einen Benutzer und gibt ein JWT zurück")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Login erfolgreich, JWT im Response-Body enthalten"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Ungültige Benutzername oder Passwort"
        )

})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        String token = authService.login(user);
        return ResponseEntity.ok(new AuthResponse(token, "Login erfolgreich"));
        
    }


    @Operation(summary = "Get User Info", description = "Ruft Informationen über den angemeldeten Benutzer ab")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Benutzerinformationen erfolgreich abgerufen"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Ungültige oder fehlende Authentifizierung"
        )
    })
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getUser( Authentication auth) {
        String message = authService.getUserInfo(auth);
        return ResponseEntity.ok(new AuthResponse(null, message));
    }
    
    
    

}
