import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import application.entities.Bike;
import application.entities.BikeRental;
import application.entities.User;
import java.sql.Timestamp;
import org.junit.jupiter.api.Test;

public class BikeRentalTest {

    @Test
    public void bikeRentalConstructorTest() {
        Bike bike = new Bike();
        User user = new User();
        BikeRental bikeRental = new BikeRental(bike, user,
            Timestamp.valueOf("2019-11-11 14:00:00"), Timestamp.valueOf("2019-11-11 15:00:00"));
        assertEquals(bikeRental.getBike(), bike);
    }

    @Test
    void bikeRentalGetIdTest() {
        BikeRental bikeRental = new BikeRental();
        assertEquals(bikeRental.getId(), 0);
    }

    @Test
    public void bikeRentalGetterAndSetterTest() {
        Bike bike = new Bike();
        Bike bike1 = new Bike();
        User user = new User();
        User user1 = new User();
        BikeRental bikeRental = new BikeRental(bike, user,
            Timestamp.valueOf("2019-11-11 14:00:00"), Timestamp.valueOf("2019-11-11 15:00:00"));
        bikeRental.setBike(bike1);
        bikeRental.setUser(user1);
        bikeRental.setStart(Timestamp.valueOf("2019-11-12 14:00:00"));
        bikeRental.setEnd(Timestamp.valueOf("2019-11-12 15:00:00"));
        assertEquals(bikeRental.getBike(), bike1);
        assertEquals(bikeRental.getUser(), user1);
        assertEquals(bikeRental.getStart(), Timestamp.valueOf("2019-11-12 14:00:00"));
        assertEquals(bikeRental.getEnd(), Timestamp.valueOf("2019-11-12 15:00:00"));
    }

    @Test
    public void bikeRentalEqualsTest() {
        Bike bike = new Bike();
        Bike bike1 = new Bike();
        User user = new User();
        User user1 = new User();
        BikeRental bikeRental1 = new BikeRental(bike, user,
            Timestamp.valueOf("2019-11-11 14:00:00"), Timestamp.valueOf("2019-11-11 15:00:00"));
        BikeRental bikeRental2 = new BikeRental(bike, user,
            Timestamp.valueOf("2019-11-11 14:00:00"), Timestamp.valueOf("2019-11-11 15:00:00"));
        assertEquals(bikeRental1, bikeRental2);
    }

    @Test
    void bikeRentalNotEqualsTest() {
        BikeRental bikeRental = new BikeRental();
        Bike bike = new Bike();
        assertNotEquals(bikeRental, bike);
    }
}
