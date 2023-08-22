import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.UserController;
import application.entities.User;
import application.entities.UserType;
import application.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        User user2 = new User(2, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userController.getAllUsers().getBody();
        assert result != null;
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getUsername(), "a");
    }

    @Test
    public void testGetUserById() {
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        user.setId(0);
        when(userRepository.findById(0)).thenReturn(java.util.Optional.of(user));
        User result = userController.getUserById(0).getBody();
        assertEquals(result, user);
    }

    @Test
    public void testCreate() {
        List<User> users = new ArrayList<>();
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userController.create(user);
        assertEquals(result.get(0), user);
    }

    @Test
    public void testUpdateUserById() {
        List<User> users = new ArrayList<>();
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        user.setId(0);
        users.add(user);
        User user1 = new User(2, "b", "ab", UserType.Employee,
            "23423", "someone@gmail.com");
        when(userRepository.findById(0)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userController.updateUserById(0, user1);
        assertEquals(result.get(0).getUsername(), "b");
    }

    @Test
    public void testDeleteUserById() {
        List<User> users = new ArrayList<>();
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        user.setId(0);
        users.add(user);
        when(userRepository.findById(0)).thenReturn(java.util.Optional.of(user));
        users.remove(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userController.deleteUserById(0);
        assertEquals(result.size(), 0);
    }
}
