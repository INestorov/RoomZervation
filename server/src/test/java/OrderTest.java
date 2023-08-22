import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Building;
import application.entities.Facility;
import application.entities.Order;
import application.entities.Reservation;
import application.entities.Room;
import application.entities.User;
import application.entities.UserType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private Order order = new Order(new Reservation(new Room(new Building("build",
        Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
        10, new HashSet<Facility>(), "descrpititt"), Timestamp.valueOf("2009-04-20 10:00:00"),
        Timestamp.valueOf("2009-04-20 10:00:01"), new User(1,
        "a", "aa", UserType.Employee,
        "23423", "someone@gmail.com")), new HashSet<>(),
        Timestamp.valueOf("2009-04-20 10:00:00"));
    private Order order4 = new Order(new Reservation(new Room(new Building("build",
        Time.valueOf("06:00:00"), Time.valueOf("20:00:00")), "abc",
        10, new HashSet<Facility>(), "descrpititt"), Timestamp.valueOf("2009-04-20 10:00:00"),
        Timestamp.valueOf("2009-04-20 10:00:01"), new User(1, "a",
        "aa", UserType.Employee,
        "23423", "someone@gmail.com")), new HashSet<>(),
        Timestamp.valueOf("2009-04-20 10:00:00"));

    @Test
    void orderConstructorTest() {
        assertEquals(order.getDate(), Timestamp.valueOf("2009-04-20 10:00:00"));
        assertEquals(order.getFoods(), new HashSet<>());
        order.setId(0);
        order.getReservation().setId(3);
        order4.getReservation().setId(3);
        order4.setId(0);
        order.getReservation().getRoom().setId(1);
        order4.getReservation().getRoom().setId(1);
        assertEquals(order.getReservation(), order4.getReservation());
    }

    @Test
    void orderDefaultConstructorTest() {
        Order test = new Order();
        assertNotNull(test);
    }

    @Test
    void orderGetIdTest() {
        assertEquals(order.getId(), 0);
    }

    @Test
    void orderGetDateTest() {
        assertEquals(order.getDate(), Timestamp.valueOf("2009-04-20 10:00:00"));
    }

    @Test
    void orderSetDateTest() {
        order.setDate(Timestamp.valueOf("2009-04-20 10:00:01"));
        assertEquals(order.getDate(), Timestamp.valueOf("2009-04-20 10:00:01"));
    }

    @Test
    void orderGetReservationTest() {
        order.getReservation().getRoom().setId(1);
        order4.getReservation().getRoom().setId(1);
        assertEquals(order.getReservation(), order4.getReservation());
    }

    @Test
    void orderSetReservationTest() {
        order.setReservation(new Reservation(new Room(),
            Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"),
            new User(1, "a", "aa", UserType.Employee,
                "23423", "someone@gmail.com")));
        order.getReservation().getRoom().setId(1);
        order4.getReservation().getRoom().setId(1);

        assertEquals(order.getReservation(), order4.getReservation());
    }

    /*
        @Test
        void orderSetGetFoodsTest() {
            HashSet<Food> foods = new HashSet<Food>();
            Food food = new Food(new Restaurant("LeGood",
                Time.valueOf("10:00:00"), Time.valueOf("10:00:00")),
                "very nice food", 5, "vegan",
                "spaghetti alle vongole");
            foods.add(food);
            order.setFoods(foods);
            assertEquals(order.getFoods(), foods);
        }
    */
    @Test
    void orderEqualsTest() {
        order.setId(0);

        Order order2 = new Order(new Reservation(new Room(new Building(), "abc",
            10, new HashSet<Facility>(), "descrpititt"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"), new User(2, "a",
            "aa", UserType.Employee,
            "23423", "someone@gmail.com")), new HashSet<>(),
            Timestamp.valueOf("2009-04-20 10:00:00"));
        Order order3 = new Order(new Reservation(new Room(new Building(), "abc",
            10, new HashSet<Facility>(), "descrpititt"), Timestamp.valueOf("2009-04-20 10:00:00"),
            Timestamp.valueOf("2009-04-20 10:00:01"), new User(1, "a",
            "aa", UserType.Employee,
            "23423", "someone@gmail.com")),
            new HashSet<>(), Timestamp.valueOf("2009-04-20 10:00:00"));
        order2.setId(1);
        order3.setId(0);
        order.getReservation().getRoom().setId(1);
        order3.getReservation().getRoom().setId(1);
        assertNotEquals(order, order2);
        assertEquals(order, order3);
    }

    @Test
    void orderNotEqualsTest() {
        assertNotEquals(order, null);
    }
}
