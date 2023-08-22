import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Facility;
import application.entities.Room;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;


public class FacilityTest {

    @Test
    void facilityDefaultConstructorTest() {
        Facility test = new Facility();
        assertNotNull(test);
    }

    @Test
    void facilityConstructorTest() {
        Facility testFacility = new Facility("TV");
        assertEquals(testFacility.getName(), "TV");
    }

    @Test
    void facilityGetNameTest() {
        Facility testFacility = new Facility("TV");
        assertEquals(testFacility.getName(), "TV");
    }

    @Test
    void facilityGetRoomsTest() {
        Facility testFacility = new Facility("TV");
        assertEquals(testFacility.getRooms(), new HashSet<Room>());
    }

    @Test
    void facilitySetRoomsTest() {
        Facility testFacility = new Facility("TV");
        Room room = new Room();
        Set<Room> set = new HashSet<>();
        set.add(room);
        testFacility.setRooms(set);
        assertEquals(testFacility.getRooms(), set);
    }

    @Test
    void facilitySetIdTest() {
        Facility testFacility = new Facility("TV");
        testFacility.setId(10);
        assertEquals(testFacility.getId(), 10);
    }

    @Test
    void facilitySetNameTest() {
        Facility testFacility = new Facility("screen");
        testFacility.setName("TV");
        assertEquals(testFacility.getName(), "TV");
    }

    @Test
    void facilityEqualsTest() {
        Facility testFacility1 = new Facility("screen");
        Facility testFacility2 = new Facility("TV");
        assertFalse(testFacility1.equals(testFacility2));
    }

    @Test
    void facilityNotEqualsTest() {
        Facility testFacility = new Facility("screen");
        assertNotEquals(testFacility, null);
    }
}
