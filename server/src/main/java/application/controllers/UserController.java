package application.controllers;

import application.entities.User;
import application.entities.UserType;
import application.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.text.html.parser.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository repository;

    /**
     * GET endpoint to get all the users.
     *
     * @return list of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = (List<User>) repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint to get an user by its id.
     *
     * @return the user
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No Users exist");
        }
    }

    /**
     * POST Endpoint to create Users.
     *
     * @return buildings
     */
    @PostMapping("/post")
    public List<User> create(@RequestBody final User user) {
        repository.save(user);
        return repository.findAll();
    }

    /**
     * PUT endpoint to update users.
     *
     * @param id     user id
     * @param entity user to be updated
     * @return all users
     * @throws IllegalArgumentException exception
     */
    @PutMapping("update/{id}")
    public List<User> updateUserById(@PathVariable("id") Integer id, @RequestBody final User entity)
        throws IllegalArgumentException {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            User newEntity = user.get();

            newEntity.setUsername((entity.getUsername()));
            newEntity.setPassword(entity.getPassword());
            newEntity.setType(entity.getType());
            newEntity.setMail(entity.getMail());
            newEntity.setPhoneNumber(entity.getPhoneNumber());

            repository.save(newEntity);

            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No user exists");
        }
    }


    /**
     * DELETE Endpoint to delete User by id.
     */
    @DeleteMapping("delete/{id}")
    public List<User> deleteUserById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<User> user = repository.findById(id);

        if (user.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No Users exist");
        }
    }

    /**
     * get type of user by username.
     *
     * @param username name of user
     * @return type of user
     */
    @GetMapping("/type/{username}")
    public String getTypeByUsername(@PathVariable("username") String username) {
        User user;
        try {
            user = repository.findByUsername(username);
            return user.getType().toString();
        } catch (Exception e) {
            System.out.println("No user found");
        }
        return null;
    }

    /** Endpoint for upgrading the user status.
     *
     * @param body content
     * @return boolean type
     */
    @PutMapping("/upgrade")
    public boolean upgradeUser(@RequestBody Map<String, Object> body) {
        ObjectMapper tree = new ObjectMapper();
        JsonNode jsonNode = tree.valueToTree(body);
        Integer userId = jsonNode.get("userId").asInt();
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            User newEntity = user.get();
            newEntity.setType(UserType.valueOf(jsonNode.get("privilege").asText()));
            repository.save(newEntity);
            return true;
        }
        return false;
    }

    /**
     * endpoint for getting the id of a user.
     * @param username the param used.
     * @return the id of user.
     */
    @GetMapping("/id/{username}")
    public int getIdOfUser(@PathVariable("username") String username) {
        User user;
        try {
            user = repository.findByUsername(username);
            return  user.getId();
        } catch (Exception e) {
            System.out.println("No user found");
        }
        return -1;
    }
}
