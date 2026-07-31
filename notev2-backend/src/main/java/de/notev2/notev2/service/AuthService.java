package de.notev2.notev2.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.notev2.notev2.config.CustomUserDetailsService;
import de.notev2.notev2.config.JwtUtil;
import de.notev2.notev2.dto.AuthLoginRequest;
import de.notev2.notev2.dto.AuthRegisterRequest;
import de.notev2.notev2.entity.User;
import de.notev2.notev2.exception.UserAlreadyExistsException;
import de.notev2.notev2.exception.InvalidCredentialsException;
import de.notev2.notev2.repos.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public String register(AuthRegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username bereits vergeben");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "Registrierung erfolgreich";
    }

    public String login(AuthLoginRequest request) {

        User existingUser = userRepository.findByUsername(request.getUsername());

        if (existingUser != null &&
            passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(existingUser.getUsername());
            return jwtUtil.generateToken(userDetails);
        }

        throw new InvalidCredentialsException("Benutzername oder Passwort falsch");
    }

    public String getUserInfo(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new InvalidCredentialsException("Ungültige Authentifizierung");
        }
        return auth.getName();
    }
}
