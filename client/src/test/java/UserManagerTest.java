import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Basket;
import application.entities.User;
import application.entities.UserManager;
import org.junit.jupiter.api.Test;

public class UserManagerTest {

    @Test
    void testGetInstance() {
        assertNotNull(UserManager.getInstance());
    }

    @Test
    void testGetUser() {
        UserManager test = UserManager.getInstance();
        assertEquals(test.getUser(), null);
    }

    @Test
    void testSetUser() {
        UserManager test = UserManager.getInstance();
        User user = new User(1);
        test.setUser(user);
        assertEquals(test.getUser(), user);
    }

    @Test
    void testGetReservationId() {
        UserManager test = UserManager.getInstance();
        assertEquals(test.getReservationId(), 0);
    }

    @Test
    void testSetReservationId() {
        UserManager test = UserManager.getInstance();
        test.setReservationId(1);
        assertEquals(test.getReservationId(), 1);
    }

    @Test
    void testSetBasket() {
        UserManager test = UserManager.getInstance();
        Basket basket = new Basket(1);
        test.setBasket(basket);
        assertEquals(test.getBasket(), basket);
    }
}
