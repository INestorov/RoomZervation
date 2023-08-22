import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.entities.Building;
import java.sql.Time;
import java.util.Objects;
import org.junit.jupiter.api.Test;



public class BuildingTest {
    @Test
    void buildingConstructorTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        assertEquals(testBuilding.getName(), "DWG1");
    }

    @Test
    void buildingGetNameTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        assertEquals(testBuilding.getName(), "DWG1");
    }

    @Test
    void buildingGetsOpeningTimeTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        assertEquals(testBuilding.getOpeningTime(), Time.valueOf("14:22:00"));
    }

    @Test
    void buildingGetClosingTimeTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        assertEquals(testBuilding.getClosingTime(), Time.valueOf("18:01:17"));
    }

    @Test
    void buildingSetNameTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        testBuilding.setName("DWG2");
        assertEquals(testBuilding.getName(), "DWG2");
    }

    @Test
    void buildingSetOpeningTimeTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        testBuilding.setOpeningTime(Time.valueOf("12:00:00"));
        assertEquals(testBuilding.getOpeningTime(), Time.valueOf("12:00:00"));
    }

    @Test
    void buildingSetClosingTimeTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        testBuilding.setClosingTime(Time.valueOf("18:00:00"));
        assertEquals(testBuilding.getClosingTime(), Time.valueOf("18:00:00"));
    }

    @Test
    void buildingSetIdTest() {
        Building testBuilding = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        testBuilding.setId(10);
        assertEquals(testBuilding.getId(), 10);
    }

    @Test
    void buildingEqualsTest() {
        Building testBuilding1 = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        testBuilding1.setId(0);
        Building testBuilding2 = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        testBuilding2.setId(testBuilding1.getId());
        assertTrue(testBuilding1.equals(testBuilding2));
    }

    @Test
    void buildingNotEqualsTest() {
        Building test = new Building();
        assertNotEquals(test, null);
    }

    @Test
    void buildingHashCodeTest() {
        Building test = new Building();
        assertEquals(test.hashCode(), Objects.hash(test.getId()));
    }
}