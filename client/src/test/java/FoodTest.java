import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Food;
import org.junit.jupiter.api.Test;

public class FoodTest {

    @Test
    void testConstructor() {
        Food test = new Food("bacon", "one", 12.0, 1, 2, "bacon");
        assertNotNull(test);

    }

    @Test
    void testConstructor2() {
        Food test = new Food("bacon", "one", 12.0, 3);
        assertNotNull(test);
    }

    @Test
    void testGetName() {
        Food test = new Food("bacon", "one", 12.0, 3);
        assertEquals(test.getName(), "bacon");
    }

    @Test
    void testSetName() {
        Food test = new Food("bacon", "one", 12.0, 3);
        test.setName("hamburger");
        assertEquals(test.getName(), "hamburger");
    }

    @Test
    void testGetDescription() {
        Food test = new Food("bacon", "one", 12.0, 3);
        assertEquals(test.getDescription(), "one");
    }

    @Test
    void testSetDescription() {
        Food test = new Food("bacon", "one", 12.0, 3);
        test.setDescription("two");
        assertEquals(test.getDescription(), "two");
    }

    @Test
    void testGetPrice() {
        Food test = new Food("bacon", "one", 12.0, 3);
        assertEquals(test.getPrice(), 12.0);
    }

    @Test
    void testSetPrice() {
        Food test = new Food("bacon", "one", 12.0, 3);
        test.setPrice(5.0);
        assertEquals(test.getPrice(), 5.0);
    }

    @Test
    void testGetRestaurantId() {
        Food test = new Food("bacon", "one", 12.0, 1, 2, "bacon");
        assertEquals(test.getRestaurantId(), 1);
    }

    @Test
    void testSetRestaurantId() {
        Food test = new Food("bacon", "one", 12.0, 1, 2, "bacon");
        test.setRestaurantId(2);
        assertEquals(test.getRestaurantId(), 2);
    }

    @Test
    void testGetId() {
        Food test = new Food("bacon", "one", 12.0, 1, 2, "bacon");
        assertEquals(test.getId(), 2);
    }

    @Test
    void testSetId() {
        Food test = new Food("bacon", "one", 12.0, 1, 2, "bacon");
        test.setId(1);
        assertEquals(test.getId(), 1);
    }

    @Test
    void testGetAmount() {
        Food test = new Food("bacon", "one", 12.0, 3);
        assertEquals(test.getAmount(), 3);
    }

    @Test
    void testSetAmount() {
        Food test = new Food("bacon", "one", 12.0, 3);
        test.setAmount(5);
        assertEquals(test.getAmount(), 5);
    }

    @Test
    void testGetType() {
        Food test = new Food("bacon", "one", 12.0, 1, 2, "bacon");
        assertEquals(test.getType(), "bacon");
    }

    @Test
    void testSetType() {
        Food test = new Food("bacon", "one", 12.0, 1, 2, "bacon");
        test.setType("bacon1");
        assertEquals(test.getType(), "bacon1");
    }
}
