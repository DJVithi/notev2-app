package de.notev2.notev2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import de.notev2.notev2.entity.User;
import de.notev2.notev2.repos.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

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
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @Operation(summary = "Get All Users", description = "Ruft alle Benutzer ab")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Benutzer erfolgreich abgerufen"
        )
    })
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
