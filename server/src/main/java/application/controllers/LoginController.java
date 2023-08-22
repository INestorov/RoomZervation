package application.controllers;

import application.entities.User;
import application.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint for updating the password.
     *
     * @param body username and pass
     * @return result of operation as boolean type
     */
    @PutMapping("/update/password")
    public boolean updatePassword(@RequestBody Map<String, Object> body) {
        ObjectMapper tree = new ObjectMapper();
        JsonNode jsonNode = tree.valueToTree(body);
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        if (user.isPresent()) {
            User newEntity = user.get();
            newEntity.setPassword(passwordEncoder.encode(password));
            userRepository.save(newEntity);
            return true;
        }
        return false;
    }
}

