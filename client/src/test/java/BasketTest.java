import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.entities.Basket;
import application.entities.Food;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BasketTest {

    @Test
    void testConstructor() {
        Basket test = new Basket(1);
        assertNotNull(test);
    }

    @Test
    void testAdd() {
        Basket test = new Basket(1);
        Food food = new Food("bacon", "one", 12.0, 1);
        test.add(food);
        assertEquals(test.getFood(0), food);
    }

    @Test
    void testGetFoods() {
        Basket test = new Basket(1);
        Food food = new Food("bacon", "one", 12.0, 1);
        List<Food> foods = new ArrayList<>();
        foods.add(food);
        test.add(food);
        assertEquals(test.getFoods().get(0), foods.get(0));
    }

    @Test
    void testGetSize() {
        Basket test = new Basket(1);
        Food food = new Food("bacon", "one", 12.0, 1);
        test.add(food);
        assertEquals(test.getSize(),1);
    }

    @Test
    void testGetFood() {
        Basket test = new Basket(1);
        Food food = new Food("bacon", "one", 12.0, 1);
        test.add(food);
        assertEquals(test.getFood(0), food);
    }

    @Test
    void testGetRestaurantId() {
        Basket test = new Basket(1);
        assertEquals(test.getRestaurantId(), 1);
    }

    @Test
    void testSetRestaurantId() {
        Basket test = new Basket(1);
        test.setRestaurantId(3);
        assertEquals(test.getRestaurantId(), 3);
    }

    @Test
    void testIsEmpty() {
        Basket test = new Basket(1);
        assertTrue(test.isEmpty());
    }

    @Test
    void testIsNotEmpty() {
        Basket test = new Basket(1);
        Food food = new Food("bacon", "one", 12.0, 1);
        test.add(food);
        assertFalse(test.isEmpty());
    }
}
