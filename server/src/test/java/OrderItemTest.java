import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Food;
import application.entities.Order;
import application.entities.OrderItem;
import java.util.Objects;
import org.junit.jupiter.api.Test;


public class OrderItemTest {

    @Test
    void testDefaultConstructor() {
        OrderItem test = new OrderItem();
        assertNotNull(test);
    }

    @Test
    void testConstructor() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        assertNotNull(test);
    }

    @Test
    void testEquals() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        OrderItem test2 = new OrderItem(food, order);
        assertEquals(test, test2);
        assertEquals(test, test);
    }

    @Test
    void testNotEquals() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        assertNotEquals(test, null);
    }

    @Test
    void testHashCode() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        assertEquals(test.hashCode(), Objects.hash(test.getFood(), test.getId()));
    }

    @Test
    void testGetAmount() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        assertEquals(test.getAmount(), 1);
    }

    @Test
    void testSetAmount() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        test.setAmount(2);
        assertEquals(test.getAmount(), 2);
    }

    @Test
    void testAddAmount() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        test.addAmount();
        assertEquals(test.getAmount(), 2);
    }

    @Test
    void testSubtractAmount() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        test.subtractAmount();
        assertEquals(test.getAmount(), 0);
    }

    @Test
    void testGetFood() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        assertEquals(test.getFood(), food);
    }

    @Test
    void testSetFood() {
        Food food = new Food();
        OrderItem test = new OrderItem();
        test.setFood(food);
        assertEquals(test.getFood(), food);
    }

    @Test
    void testGetOrder() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        assertEquals(test.getOrder(), order);
    }

    @Test
    void testSetOrder() {
        Order order = new Order();
        OrderItem test = new OrderItem();
        test.setOrder(order);
        assertEquals(test.getOrder(), order);
    }

    @Test
    void testGetId() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        assertEquals(test.getId(), 0);
    }

    @Test
    void testSetId() {
        Food food = new Food();
        Order order = new Order();
        OrderItem test = new OrderItem(food, order);
        test.setId(2);
        assertEquals(test.getId(), 2);
    }

}
