import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.OrderRequestBody;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OrderRequestBodyTest {

    @Test
    void testConstructor() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        assertNotNull(test);
    }

    @Test
    void testGetReservationId() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        assertEquals(test.getReservationId(), 1);
    }

    @Test
    void testGetRestaurantId() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        assertEquals(test.getRestaurantId(), 2);
    }

    @Test
    void testSetRestaurantId() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        test.setRestaurantId(3);
        assertEquals(test.getRestaurantId(), 3);
    }

    @Test
    void testGetDate() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        assertEquals(test.getDate(), Timestamp.valueOf("2019-11-11 14:00:00"));
    }

    @Test
    void testSetDate() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        test.setDate(Timestamp.valueOf("2019-11-11 15:00:00"));
        assertEquals(test.getDate(), Timestamp.valueOf("2019-11-11 15:00:00"));
    }

    @Test
    void testGetFoodIds() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        assertEquals(test.getFoodIds(), new ArrayList<>());
    }

    @Test
    void testSetFoodIds() {
        OrderRequestBody test = new OrderRequestBody(1, 2,
            Timestamp.valueOf("2019-11-11 14:00:00"), new ArrayList<>());
        List<Integer> list = new ArrayList<>();
        list.add(1);
        test.setFoodIds(list);
        assertEquals(test.getFoodIds(), list);
    }
}
