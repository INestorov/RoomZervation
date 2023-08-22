import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Order;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    void testConstructor() {
        Order test = new Order(1, 2, 2020);
        assertNotNull(test);
    }

    @Test
    void testGetReservationId() {
        Order test = new Order(1, 2, 2020);
        assertEquals(test.getReservationId(),1);
    }

    @Test
    void testGetRestaurantId() {
        Order test = new Order(1, 2, 2020);
        assertEquals(test.getRestaurantId(),2);
    }

    @Test
    void testSetRestaurantId() {
        Order test = new Order(1, 2, 2020);
        test.setRestaurantId(3);
        assertEquals(test.getRestaurantId(),3);
    }

    @Test
    void testGetDate() {
        Order test = new Order(1, 2, 2020);
        assertEquals(test.getDate(),2020);
    }

    @Test
    void testSetDate() {
        Order test = new Order(1, 2, 2020);
        test.setDate(2021);
        assertEquals(test.getDate(),2021);
    }

    @Test
    void testGetFoodIds() {
        Order test = new Order(1, 2, 2020);
        List<Integer> ids = new ArrayList<>();
        assertEquals(test.getFoodIds(), ids);
    }

    @Test
    void testSetFoodIds() {
        Order test = new Order(1, 2, 2020);
        List<Integer> ids = new ArrayList<>();
        ids.add(3);
        test.setFoodIds(ids);
        assertEquals(test.getFoodIds(), ids);
    }
}
