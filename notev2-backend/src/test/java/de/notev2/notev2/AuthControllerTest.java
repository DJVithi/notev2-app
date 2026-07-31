package de.notev2.notev2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional; // WICHTIG!


import de.notev2.notev2.entity.User;
import de.notev2.notev2.repos.UserRepository;

// Falls du ein Passwort-Encoder nutzt, aktiviere den Import:
// import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Test
    void login_shouldReturnToken() throws Exception {

        User user = new User();
        user.setUsername("deniz");
        user.setPassword(passwordEncoder.encode("123"));
        userRepository.save(user);
        
        String request = """
            {
                "username": "deniz",
                "password": "123"
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_withInvalidCredentials_shouldReturnUnauthorized() throws Exception {
     
        User user = new User();
        user.setUsername("deniz");
        user.setPassword(passwordEncoder.encode("123"));
        userRepository.save(user);

        String request = """
            {
                "username": "deniz",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withNonExistingUser_shouldReturnUnauthorized() throws Exception {
    
        String request = """
            {
                "username": "nonexistinguser",
                "password": "123"
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withEmptyUsername_shouldReturnBadRequest() throws Exception {
    
        String request = """
            {
                "username": "",
                "password": "123"
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username").exists());
    }

    @Test
    void login_withEmptyPassword_shouldReturnBadRequest() throws Exception {
    
        String request = """
            {
                "username": "deniz",
                "password": ""
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    void login_withEmptyUsernameAndPassword_shouldReturnBadRequest() throws Exception {
    
        String request = """
            {
                "username": "",
                "password": ""
            }
            """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.password").exists());

    }
    @Test
    void login_withMaxLengthUsername_shouldReturnBadRequest() throws Exception {
        String username = "a".repeat(51);
        String request = """
            {
                "username": "%s",
                "password": "123"
            }
            """.formatted(username);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username").exists());
    }
    @Test
    void login_withMaxLengthPassword_shouldReturnBadRequest() throws Exception {
        String password = "a".repeat(51);
        String request = """
            {
                "username": "deniz",
                "password": "%s"
            }
            """.formatted(password);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.password").exists());
    }
    
            

    @Test
    void register_withExistingUsername_shouldReturnBadRequest() throws Exception {
     
        String request = """
            {
                "username": "deniz",
                "password": "123"
            }
            """;

        
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isConflict());
    }

    @Test
    void register_withEmptyUsername_shouldReturnBadRequest() throws Exception {
        String request = """
            {
                "username" : "",
                "password" : "123"
            }
            """;
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username")
                        .exists());
            
    }
    @Test
    void register_withEmptyPassword_shouldReturnBadRequest() throws Exception {
        String request = """
            {
                "username" : "deniz",
                "password" : ""
            }
            """;
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.password")
                        .exists());
            
    }
    @Test
    void register_withEmptyUsernamePassword_shouldReturnBadRequest() throws Exception {
        String request = """
            {
                "username" : "",
                "password" : ""
            }
            """;
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username")
                        .exists()) 
                .andExpect(jsonPath("$.errors.password")
                        .exists());
            
    }
    @Test
    void register_withEmptyUsernameAndwithSpacesPassword_shouldReturnBadRequest() throws Exception {
        String request = """
            {
                "username" : "",
                "password" : "   "
            }
            """;
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username")
                        .exists()) 
                .andExpect(jsonPath("$.errors.password")
                        .exists());
            
    }
    @Test
    void register_withSpacesUsernameAndwithEmptyPassword_shouldReturnBadRequest() throws Exception {
        String request = """
            {
                "username" : "   ",
                "password" : ""
            }
            """;
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username")
                        .exists()) 
                .andExpect(jsonPath("$.errors.password")
                        .exists());
            
    }
    @Test
    void register_withMaxLengthUsername_shouldReturnBadRequest() throws Exception {
        String longUsername = "a".repeat(51);
        String request = """
            {
                "username" : "%s",
                "password" : "123"
            }
            """.formatted(longUsername);
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.username")
                        .exists());
            
    }
    @Test
    void register_withMaxLengthPassword_shouldReturnBadRequest() throws Exception {
        String longPassword = "a".repeat(51);
        String request = """
            {
                "username" : "deniz",
                "password" : "%s"
            }
            """.formatted(longPassword);
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Validierung fehlgeschlagen"))
                .andExpect(jsonPath("$.errors.password")
                        .exists());
            
    }
}