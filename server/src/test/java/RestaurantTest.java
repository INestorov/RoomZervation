import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.entities.Building;
import application.entities.Restaurant;
import java.sql.Time;
import org.junit.jupiter.api.Test;

public class RestaurantTest {
    private Building building = new Building("DWG1",
        Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));

    private Building otherBuilding = new Building("name",
        Time.valueOf("16:20:01"), Time.valueOf("18:19:19"));

    private Restaurant test = new Restaurant("The Corona",
        Time.valueOf("19:00:00"), Time.valueOf("00:00:00"), building);

    @Test
    void constructorTest() {
        Restaurant test2 = new Restaurant("The Corona",
            Time.valueOf("12:00:00"), Time.valueOf("15:00:00"), building);
        assertNotEquals(test, test2);
    }

    @Test
    void getNameTest() {
        assertEquals(test.getName(), "The Corona");
    }

    @Test
    void setNameTest() {
        test.setName("The Cure");
        assertEquals(test.getName(), "The Cure");
    }

    @Test
    void getOpenTest() {
        assertEquals(test.getOpeningTime(), Time.valueOf("19:00:00"));
    }

    @Test
    void setOpenTest() {
        test.setOpeningTime(Time.valueOf("04:20:00"));
        assertEquals(test.getOpeningTime(), Time.valueOf("04:20:00"));
    }

    @Test
    void getClosingTest() {
        assertEquals(test.getClosingTime(), Time.valueOf("00:00:00"));
    }

    @Test
    void setClosingTest() {
        test.setClosingTime(Time.valueOf("13:00:00"));
        assertEquals(test.getClosingTime(), Time.valueOf("13:00:00"));
    }

    @Test
    void getBuildingTest() {
        assertEquals(building, test.getBuilding());
    }

    @Test
    void setBuildingTest() {
        test.setBuilding(otherBuilding);
        assertEquals(otherBuilding, test.getBuilding());
    }

    @Test
    void getIdTest() {
        assertEquals(test.getId(), 0);
    }

    @Test
    void equalsTest() {
        Restaurant resti = new Restaurant("bobo",
            Time.valueOf("13:00:00"), Time.valueOf("15:00:00"), building);
        Restaurant testi = new Restaurant("The Corona",
            Time.valueOf("19:00:00"), Time.valueOf("00:00:00"), building);
        assertFalse(test.equals(resti));
        assertTrue(test.equals(testi));
    }

    @Test
    void notEqualsTest() {
        assertNotEquals(test, null);
    }
}
