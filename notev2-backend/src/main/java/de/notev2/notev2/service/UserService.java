package de.notev2.notev2.service;



import de.notev2.notev2.dto.UserRegisterRequest;
import de.notev2.notev2.dto.UserResponse;
import de.notev2.notev2.entity.User;
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

    public String register(UserRegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username bereits vergeben");
        }
        

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAdmin(request.isAdmin());


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
