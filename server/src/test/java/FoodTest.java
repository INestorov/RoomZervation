import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.entities.Building;
import application.entities.Food;
import application.entities.Restaurant;
import java.sql.Time;
import org.junit.jupiter.api.Test;

public class FoodTest {

    private Building building = new Building("DWG1",
        Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));

    private Food food = new Food(new Restaurant("LeGood",
        Time.valueOf("14:25:00"), Time.valueOf("16:25:00"), building),
        "very nice food", 5, "vegan", "spaghetti alle vongole");

    @Test
    void foodConstructorTest() {
        assertEquals(food.getDescription(), "very nice food");
        assertEquals(food.getPrice(), 5);
        assertEquals(food.getName(), "spaghetti alle vongole");
    }

    @Test
    void foodDefaultConstructorTest() {
        Food test = new Food();
        assertNotNull(test);
    }

    @Test
    void foodGetNameTest() {
        assertEquals(food.getName(), "spaghetti alle vongole");
    }

    @Test
    void foodSetNameTest() {
        food.setName("gnocchi");
        assertEquals(food.getName(), "gnocchi");
    }

    @Test
    void foodGetDescriptionTest() {
        assertEquals(food.getDescription(), "very nice food");
    }

    @Test
    void foodGetsPriceTest() {
        assertEquals(food.getPrice(), 5);
    }

    @Test
    void foodGetRestaurantTest() {

        assertEquals(food.getRestaurant(), new Restaurant("LeGood",
            Time.valueOf("14:25:00"), Time.valueOf("16:25:00"), building));
    }

    @Test
    void foodSetDescriptionTest() {
        food.setDescription("testitest");
        assertEquals(food.getDescription(), "testitest");
    }

    @Test
    void foodSetPriceTest() {
        food.setPrice(26);
        assertEquals(food.getPrice(), 26);
    }

    @Test
    void foodSetClosingTimeTest() {
        Restaurant restiTest = new Restaurant(("FoodGood"),
            Time.valueOf("14:25:00"), Time.valueOf("20:25:00"), building);
        food.setRestaurant(restiTest);
        assertEquals(food.getRestaurant(), restiTest);
    }

    @Test
    void foodSetIdTest() {
        food.setId(10);
        assertEquals(food.getId(), 10);
    }

    @Test
    void foodEqualsTest() {
        Food menu2 = new Food(new Restaurant(), "nice", 56, "meat", "gnocchi");
        Food menu3 = new Food(new Restaurant("LeGood", Time.valueOf("14:25:00"),
            Time.valueOf("16:25:00"), building), "very nice food",
            5, "vegan", "spaghetti alle vongole");
        assertNotEquals(food, menu2);
        assertEquals(food, menu3);
    }

    @Test
    void foodNotEqualsTest() {
        assertNotEquals(food, null);
    }
}
