import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Building;
import application.entities.Restaurant;
import application.entities.Room;
import java.sql.Time;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class RestaurantTest {

    @Test
    void testConstructor() {
        Building building = new Building("Aula", 5, 1,
            new Time(System.currentTimeMillis()), new Time(System.currentTimeMillis() + 1));
        Restaurant test = new Restaurant("food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), building);
        assertNotNull(test);
    }

    @Test
    void testConstructor2() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertNotNull(test);
    }

    @Test
    void testGetName() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertEquals(test.getName(), "food");

    }

    @Test
    void testSetName() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        test.setName("EWI");
        assertEquals(test.getName(), "EWI");
    }

    @Test
    void testGetId() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertEquals(test.getId(), 1);
    }

    @Test
    void testSetId() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        test.setId(2);
        assertEquals(test.getId(), 2);
    }

    @Test
    void testGetOpeningTime() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertEquals(test.getOpeningTime(), Time.valueOf("16:00:00"));
    }

    @Test
    void testSetOpeningTime() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        test.setOpeningTime(Time.valueOf("17:00:00"));
        assertEquals(test.getOpeningTime(), Time.valueOf("17:00:00"));
    }

    @Test
    void testGetClosingTime() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertEquals(test.getClosingTime(), Time.valueOf("17:00:00"));
    }

    @Test
    void testSetClosingTime() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        test.setClosingTime(Time.valueOf("16:00:00"));
        assertEquals(test.getClosingTime(), Time.valueOf("16:00:00"));
    }

    @Test
    void testGetLocationTest() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertEquals(test.getLocation(), "Aula");
    }

    @Test
    void testSetLocationTest() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        test.setLocation("EWI");
        assertEquals(test.getLocation(), "EWI");
    }

    @Test
    void testGetBuilding() {
        Building building = new Building("Aula", 5, 1,
            new Time(System.currentTimeMillis()), new Time(System.currentTimeMillis() + 1));
        Restaurant test = new Restaurant("food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), building);
        assertEquals(test.getBuilding(), building);
    }

    @Test
    void testSetBuilding() {
        Building building = new Building("Aula", 5, 1,
            new Time(System.currentTimeMillis()), new Time(System.currentTimeMillis() + 1));
        Restaurant test = new Restaurant("food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), null);
        test.setBuilding(building);
        assertEquals(test.getBuilding(), building);
    }

    @Test
    void testEquals() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        Restaurant test2 = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertEquals(test, test2);
        assertEquals(test, test);
    }

    @Test
    void testNotEquals() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertNotEquals(test, null);
    }

    @Test
    void testHashCode() {
        Restaurant test = new Restaurant(1, "food", Time.valueOf("16:00:00"),
            Time.valueOf("17:00:00"), "Aula");
        assertEquals(test.hashCode(), Objects.hash(test.getId(), test.getName(),
            test.getOpeningTime(), test.getClosingTime(), test.getLocation()));
    }
}
