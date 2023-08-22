import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.User;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void testConstructor() {
        User test = new User(1);
        assertNotNull(test);
    }

    @Test
    void testGetId() {
        User test = new User(1);
        assertEquals(test.getId(), 1);
    }

    @Test
    void testSetId() {
        User test = new User(1);
        test.setId(2);
        assertEquals(test.getId(), 2);
    }

    @Test
    void testEquals() {
        User test = new User(1);
        User test2 = new User(1);
        assertEquals(test, test2);
    }

    @Test
    void testEquals2() {
        User test = new User(1);
        assertEquals(test, test);
    }

    @Test
    void testNotEquals() {
        User test = new User(1);
        User test2 = new User(2);
        assertNotEquals(test, test2);
    }

    @Test
    void testNotEquals2() {
        User test = new User(1);
        assertNotEquals(test, null);
    }

    @Test
    void testHashCode() {
        User test = new User(1);
        assertEquals(test.hashCode(), Objects.hash(test.getId()));
    }
}
