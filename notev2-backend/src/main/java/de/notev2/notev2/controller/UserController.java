package de.notev2.notev2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import de.notev2.notev2.dto.UserRegisterRequest;
import de.notev2.notev2.dto.UserResponse;
import de.notev2.notev2.service.UserService;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create User", description = "Erstellt einen neuen Benutzer")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Benutzer erfolgreich erstellt"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Fehlende oder ungültige Felder"
        )
    })
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRegisterRequest user) {

        String result = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Get All Users", description = "Ruft alle Benutzer ab")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Benutzer erfolgreich abgerufen"
        )
    })
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}
