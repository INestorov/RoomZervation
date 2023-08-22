package application.controllers;

import application.entities.ServerResponse;
import application.entities.User;
import application.entities.UserType;
import application.repositories.UserRepository;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Logger logger;

    /**
     * Creates an account.
     *
     * @param registration JSON string containing id (number), username (text), password (text),
     *                     email (text, optional) and phoneNumber (text, optional)
     * @return {@link application.entities.ServerResponse.ServerErrorResponse} if failed to create
     *         an account, otherwise, {@code 201 Created}
     */
    @PostMapping
    public ResponseEntity<ServerResponse> doPost(@RequestBody Registration registration) {
        try {
            registration.validate();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ServerResponse.error(HttpServletResponse.SC_BAD_REQUEST, e.getMessage()));
        }
        User user = userRepository.findByIdOrUsername(registration.id, registration.username);
        if (user != null) {
            return ResponseEntity.badRequest().body(ServerResponse
                .error(HttpServletResponse.SC_BAD_REQUEST, String
                    .format("Account with same %s already exists",
                        user.getId() == registration.id ? "id" : "username")));
        }
        userRepository.save(new User(registration.id, registration.username,
            passwordEncoder.encode(registration.password), UserType.Student,
            registration.phoneNumber, registration.email));
        logger.info("User with id {} and username {} was created", registration.id,
            registration.username);
        return ResponseEntity
            .created(URI.create(String.format("https://localhost:8443/users/%d", registration.id)))
            .build();
    }

    private static final class Registration {
        public Integer id;
        public String username;
        public String password;
        public String email;
        public String phoneNumber;

        public void validate() throws Exception {
            if (id == null) {
                throw new Exception("Id is required");
            } else if (id <= 0) {
                throw new Exception("Id must be greater than 0");
            } else if (username == null) {
                throw new Exception("Username is required");
            } else if (!username.matches("\\w+")) {
                throw new Exception("Username must only contain alphanumeric characters");
            } else if (username.length() < 4 || username.length() > 25) {
                throw new Exception("Username must be between 4 and 25 characters");
            } else if (password == null) {
                throw new Exception("Password is required");
            } else if (password.length() < 8) {
                throw new Exception("Password must be at least 8 characters long");
            } else if (email != null && !email
                .matches("[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*")) {
                throw new Exception("Invalid email");
            } else if (phoneNumber != null && !phoneNumber.matches("[+]\\d+")) {
                throw new Exception("Invalid phone number");
            }
        }
    }
}
