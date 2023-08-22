import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import application.entities.Building;
import application.entities.Facility;
import application.entities.Room;
import java.sql.Time;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;


public class RoomTest {
    @Test
    void roomConstructorTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        assertEquals(building, roomTest.getBuilding());
    }

    @Test
    void roomGetBuildingTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        assertEquals(building, roomTest.getBuilding());
    }

    @Test
    void roomGetNameTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        assertEquals(roomTest.getName(), "abc");
    }

    @Test
    void roomGetSizeTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        assertEquals(roomTest.getSize(), 10);
    }

    @Test
    void roomSetIdTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        roomTest.setId(10);
        assertEquals(roomTest.getId(), 10);
    }

    @Test
    void roomSetBuildingTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Building building1 = new Building("DW", Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        roomTest.setBuilding(building1);
        assertEquals(roomTest.getBuilding(), building1);
    }

    @Test
    void roomSetNameTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        roomTest.setName("aa");
        assertEquals(roomTest.getName(), "aa");
    }

    @Test
    void roomSetSizeTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        roomTest.setSize(15);
        assertEquals(roomTest.getSize(), 15);
    }

    @Test
    void roomEqualsTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest1 = new Room(building, "abc", 10, facilities, "nice");
        Room roomTest2 = new Room(building, "aaa", 15, facilities, "nice");
        roomTest1.setId(1);
        roomTest2.setId(2);
        assertFalse(roomTest1.equals(roomTest2));
    }

    @Test
    void roomNotEqualsTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        assertNotEquals(roomTest, null);
    }

    @Test
    void roomHashCodeTest() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room roomTest = new Room(building, "abc", 10, facilities, "nice");
        assertEquals(roomTest.hashCode(), Objects.hash(roomTest.getId()));
    }
}
