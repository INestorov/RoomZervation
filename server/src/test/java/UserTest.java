import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.BikeRental;
import application.entities.User;
import application.entities.UserType;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void userDefaultConstructorTest() {
        User test = new User();
        assertNotNull(test);
    }

    @Test
    void userConstructorTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        assertEquals(userTest.getUsername(), "a");
    }

    @Test
    void userGetUserNameTest() {
        User userTest = new User(1, "b", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        assertEquals(userTest.getUsername(), "b");
    }

    @Test
    void userSetUserNameTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        userTest.setUsername("b");
        assertEquals(userTest.getUsername(), "b");
    }


    @Test
    void userGetPasswordTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        assertEquals(userTest.getPassword(), "aa");
    }

    @Test
    void userSetPassWordTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        userTest.setPassword("abc");
        assertEquals(userTest.getPassword(), "abc");
    }

    @Test
    void userGetTypeTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        assertEquals(userTest.getType(), UserType.Employee);
    }

    @Test
    void userSetTypeTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        userTest.setType(UserType.Employee);
        assertEquals(userTest.getType(), UserType.Employee);
    }

    @Test
    void userSetUserNumberTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        userTest.setId(123);
        assertEquals(userTest.getId(), 123);
    }

    @Test
    void userEqualsTest() {
        User userTest1 = new User(1, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        User userTest2 = new User(2, "a", "aa", UserType.Employee,
            "+32134", "someone@gmail.com");
        User userTest3 = new User(3, "a", "aa", UserType.Admin,
            "+32232", "someone@gmail.com");
        userTest1.setId(1);
        userTest2.setId(2);
        userTest3.setId(userTest2.getId());
        assertNotEquals(userTest1, userTest2);
        assertNotEquals(userTest2, userTest3);
    }

    @Test
    void userNotEqualsTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+22222", "someone@gmail.com");
        assertNotEquals(userTest, null);
    }

    @Test
    void userGetRentalBikesTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+22222", "someone@gmail.com");
        assertEquals(userTest.getBikeRentals(), new HashSet<>());
    }

    @Test
    void userSetRentalBikesTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+22222", "someone@gmail.com");
        BikeRental bikeRental = new BikeRental();
        Set<BikeRental> rentalSet = new HashSet<>();
        rentalSet.add(bikeRental);
        userTest.setBikeRentals(rentalSet);
        assertEquals(userTest.getBikeRentals(), rentalSet);
    }

    @Test
    void userHashCodeTest() {
        User userTest = new User(1, "a", "aa", UserType.Employee,
            "+22222", "someone@gmail.com");
        assertEquals(userTest.hashCode(), Objects.hash(userTest.getId(), userTest.getType()));
    }
}
