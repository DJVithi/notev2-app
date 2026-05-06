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





@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        /*if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(400).body(new AuthResponse(null, "Benutzername und/ oder Passwort sind erforderlich"));
        }


        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(409).body(new AuthResponse(null, "Username bereits vergeben"));
        }
        

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new AuthResponse(null, "Registrierung erfolgreich"));
    }*/
        String message = authService.register(user);
        return ResponseEntity.ok(new AuthResponse(null, message));
        
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
       /*User existinguser = userRepository.findByUsername(user.getUsername());
        
        
        if (existinguser != null && passwordEncoder.matches(user.getPassword(), existinguser.getPassword())) {
            String token = jwtUtil.generateToken(existinguser.getUsername());
            AuthResponse response = new AuthResponse(token, "login erfolgreich");
            return ResponseEntity.ok(response);
        } else {
            AuthResponse response = new AuthResponse(null, "Benutzername oder Passwort falsch");
            return ResponseEntity.status(401).body(response);
        }
    }*/
        
        String token = authService.login(user);
        return ResponseEntity.ok(new AuthResponse(token, "Login erfolgreich"));
        
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getUser( Authentication auth) {
        String message = authService.getUserInfo(auth);
        return ResponseEntity.ok(new AuthResponse(null, message));
    }
    
    
    

}
