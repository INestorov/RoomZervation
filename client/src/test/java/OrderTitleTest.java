import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.OrderTitle;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class OrderTitleTest {

    @Test
    void testConstructor() {

        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        assertNotNull(test);
    }

    @Test
    void testGetPrice() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        assertEquals(test.getPrice(), 12.0);
    }

    @Test
    void testSetPrice() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        test.setPrice(14.0);
        assertEquals(test.getPrice(), 14.0);
    }

    @Test
    void testGetName() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        assertEquals(test.getName(), "Pizza");
    }

    @Test
    void testSetName() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        test.setName("Bacon");
        assertEquals(test.getName(), "Bacon");
    }

    @Test
    void testGetTime() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        assertEquals(test.getTime(), new Date(2020));
    }

    @Test
    void testSetTime() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        test.setTime(new Date(2021));
        assertEquals(test.getTime(), new Date(2021));
    }

    @Test
    void testGetId() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        assertEquals(test.getId(), 1);
    }

    @Test
    void testSetId() {
        OrderTitle test = new OrderTitle(12.0, "Pizza", new Date(2020),1);
        test.setId(2);
        assertEquals(test.getId(), 2);
    }
}
