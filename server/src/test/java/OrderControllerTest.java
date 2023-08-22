import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import application.controllers.OrderController;
import application.entities.Building;
import application.entities.Facility;
import application.entities.Food;
import application.entities.Order;
import application.entities.OrderItem;
import application.entities.Reservation;
import application.entities.Restaurant;
import application.entities.Room;
import application.entities.User;
import application.entities.UserType;
import application.repositories.OrderItemRepository;
import application.repositories.OrderRepository;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderItemRepository orderItemRepository;

    @Test
    void readAllTest() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"),
            new User(1, "a", "aa", UserType.Employee,
                "23423", "someone@gmail.com")),
            new HashSet<>(), Timestamp.valueOf("2009-04-20 10:00:00"));
        orders.add(order);
        order.setId(0);
        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderController.getAllOrders().getBody();
        assertEquals(result.size(), 1);
    }

    @Test
    void getOrderByIdTest() {
        Order order = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")),
            "abc", 10, new HashSet<Facility>(), "descrptit"),
            Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"),
            new User(1, "a", "aa",
                UserType.Employee,
                "23423", "someone@gmail.com")),
            new HashSet<>(), Timestamp.valueOf("2009-04-20 10:00:00"));
        Order order2 = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"),
            new User(1, "a", "aa", UserType.Employee,
                "23423", "someone@gmail.com")),
            new HashSet<>(), Timestamp.valueOf("2009-04-20 10:00:00"));
        order.setId(0);
        order2.setId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        Order result = orderController.getOrderById(1).getBody();
        assertEquals(result, order);
    }

    @Test
    void updateTest() {
        final List<Order> orders = new ArrayList<>();
        Order order = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"), new User(1,
            "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com")), new HashSet<>(),
            Timestamp.valueOf("2009-04-20 10:00:00"));
        order.setId(0);
        orders.add(order);
        Order order2 = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:02"), new User(1,
            "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com")), new HashSet<>(),
            Timestamp.valueOf("2019-04-20 10:00:00"));
        order2.setId(1);
        when(orderRepository.findById(0)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderController.updateOrderById(0, order2);
        assertEquals(result.get(0).getDate(), order2.getDate());
    }

    @Test
    public void testDeleteOrderById() {
        final List<Order> orders = new ArrayList<>();
        Order order = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"), new User(1,
            "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com")), new HashSet<>(),
            Timestamp.valueOf("2009-04-20 10:00:00"));
        order.setId(0);
        orders.add(order);
        when(orderRepository.findById(0)).thenReturn(java.util.Optional.of(order));
        orders.remove(order);
        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderController.deleteOrderById(0);
        assertEquals(result.size(), 0);
    }

    @Test
    public void testAddFood() {
        Order order = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"), new User(1, "a",
            "aa", UserType.Employee,
            "23423", "someone@gmail.com")), new HashSet<>(),
            Timestamp.valueOf("2009-04-20 10:00:00"));
        order.setId(0);
        Food toAdd = new Food(new Restaurant(), "goodfood", 12, "meaty",
            "good");
        toAdd.setId(0);
        OrderItem orderItem = new OrderItem(toAdd, order);
        when(orderRepository.findById(0)).thenReturn(java.util.Optional.of(order));
        Optional<Order> result = orderController.addFood(toAdd, 0);
        assertEquals(result.get().getFoods().size(), 1);
        assertTrue(result.get().getFoods().contains(orderItem));
    }

    @Test
    public void deleteFoodAmountOneTest() {
        Order order = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"), new User(1, "a",
            "aa", UserType.Employee,
            "23423", "someone@gmail.com")), new HashSet<>(),
            Timestamp.valueOf("2009-04-20 10:00:00"));
        order.setId(0);
        Food toAdd = new Food(new Restaurant(), "goodfood", 12, "meaty",
            "good");
        toAdd.setId(0);
        OrderItem orderItem = new OrderItem(toAdd, order);
        order.getFoods().add(orderItem);
        when(orderRepository.findById(0)).thenReturn(java.util.Optional.of(order));
        when(orderItemRepository.findOrderItemsByFood_IdAndOrder_Id(0, 0))
            .thenReturn(java.util.Optional.of(orderItem));
        Optional<Order> result = orderController.deleteFoodFromOrder(0, toAdd);
        assertEquals(0, result.get().getFoods().size());
        assertTrue(result.get().getFoods().isEmpty());
    }

    @Test
    public void deleteFoodMultiplesTest() {
        Order order = new Order(new Reservation(new Room(new Building("build",
            Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
            10, new HashSet<Facility>(), "descrptit"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"), new User(1, "a",
            "aa", UserType.Employee,
            "23423", "someone@gmail.com")),
            new HashSet<>(), Timestamp.valueOf("2009-04-20 10:00:00"));
        order.setId(0);
        Food toAdd = new Food(new Restaurant(), "goodfood", 12, "meaty",
            "good");
        toAdd.setId(0);
        OrderItem orderItem = new OrderItem(toAdd, order);
        orderItem.setAmount(3);
        order.getFoods().add(orderItem);
        when(orderRepository.findById(0)).thenReturn(java.util.Optional.of(order));
        when(orderItemRepository.findOrderItemsByFood_IdAndOrder_Id(0, 0))
            .thenReturn(java.util.Optional.of(orderItem));
        orderController.deleteFoodFromOrder(0, toAdd);
        Optional<Order> result = orderController.deleteFoodFromOrder(0, toAdd);
        assertEquals(1, result.get().getFoods().size());
        Optional<Order> secondResult = orderController.deleteFoodFromOrder(0, toAdd);
        assertEquals(0, secondResult.get().getFoods().size());
    }
}
