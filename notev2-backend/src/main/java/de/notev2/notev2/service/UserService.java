package de.notev2.notev2.service;



import de.notev2.notev2.dto.UserResponse;
import de.notev2.notev2.entity.User;
import de.notev2.notev2.exception.EmptyFieldException;
import de.notev2.notev2.exception.UserAlreadyExistsException;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import de.notev2.notev2.repos.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(de.notev2.notev2.repos.UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(User user) {

        if (user.getUsername() == null || user.getUsername().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new EmptyFieldException("Benutzername und/oder Passwort fehlen");
        }

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username bereits vergeben");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "Registrierung erfolgreich";
    }

    public List<UserResponse> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getAdmin()))
        .toList();
}

}
