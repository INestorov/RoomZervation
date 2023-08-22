import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.BikeRental;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class BikeRentalTest {

    @Test
    void testConstructor() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        assertNotNull(test);
    }


    @Test
    void testGetBikeId() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        assertEquals(test.getBikeId(),1);
    }

    @Test
    void testSetBikeId() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        test.setBikeId(2);
        assertEquals(test.getBikeId(),2);
    }

    @Test
    void testGetUserId() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        assertEquals(test.getUserId(),2);
    }

    @Test
    void testSetUserId() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        test.setUserId(1);
        assertEquals(test.getUserId(),1);
    }

    @Test
    void testGetStartTime() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        assertEquals(test.getStartTime(),"16:00");
    }

    @Test
    void testSetStartTime() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        test.setStartTime("17:00");
        assertEquals(test.getStartTime(),"17:00");
    }

    @Test
    void testGetEndTime() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        assertEquals(test.getEndTime(),"17:00");
    }

    @Test
    void testSetEndTime() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        test.setEndTime("16:00");
        assertEquals(test.getEndTime(),"16:00");
    }

    @Test
    void testEquals() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        BikeRental test2 = new BikeRental(1,2,"16:00", "17:00");
        assertEquals(test, test2);
        assertEquals(test, test);
    }

    @Test
    void testNotEquals() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        assertNotEquals(test, null);
    }

    @Test
    void testHashCode() {
        BikeRental test = new BikeRental(1,2,"16:00", "17:00");
        assertEquals(test.hashCode(), Objects.hash(test.getBikeId(),
            test.getUserId(), test.getStartTime(), test.getEndTime()));
    }

}
