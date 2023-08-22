import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import application.entities.Bike;
import application.entities.BikeRental;
import application.entities.Building;
import application.entities.User;
import application.entities.UserType;
import java.sql.Time;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class BikeTest {
    @Test
    public void bikeConstructorTest() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike = new Bike(building);
        assertEquals(bike.getBuilding(), building);
    }

    @Test
    public void bikeGetterAndSetterTest() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike = new Bike();
        bike.setBuilding(building);
        assertEquals(bike.getBuilding(), building);
    }

    @Test
    public void bikeEqualsTest() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike = new Bike(building);
        Bike bike1 = new Bike(building);
        assertEquals(bike, bike1);
    }

    @Test
    void bikeNotEqualsTest() {
        Bike bike = new Bike();
        assertNotEquals(bike, null);
    }

    @Test
    void bikeGetIdTest() {
        Bike bike = new Bike();
        assertEquals(bike.getId(), 0);
    }

    @Test
    void bikeHashCodeTest() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike = new Bike(building);
        assertEquals(bike.hashCode(), Objects.hash(bike.getId(), bike.getBuilding()));
    }

    @Test
    void bikeGetUserTest() {
        Bike bike = new Bike();
        assertEquals(bike.getUser(), null);
    }

    @Test
    void bikeSetUserTest() {
        Bike bike = new Bike();
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        bike.setUser(userTest);
        assertEquals(bike.getUser(), userTest);
    }

    @Test
    void bikeAddBikeRentalTest() {
        Bike bike = new Bike();
        BikeRental bikeRental = new BikeRental();
        Set<BikeRental> set = new HashSet<>();
        set.add(bikeRental);
        bike.addBikeRental(bikeRental);
        assertEquals(bike.getBikeRentals(), set);
    }

}
