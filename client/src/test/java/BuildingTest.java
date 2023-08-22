import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Building;
import application.entities.Room;
import java.sql.Time;
import org.junit.jupiter.api.Test;

public class BuildingTest {

    private Time openingTime = new Time(System.currentTimeMillis());
    private Time closingTime = new Time(System.currentTimeMillis() + 1);

    @Test
    void testConstructor() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        assertNotNull(test);
    }

    @Test
    void testConstructor2() {
        Building test = new Building("Aula");
        assertNotNull(test);
    }

    @Test
    void testGetName() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        assertEquals(test.getName(), "Aula");

    }

    @Test
    void testSetName() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        test.setName("EWI");
        assertEquals(test.getName(), "EWI");
    }

    @Test
    void testGetId() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        assertEquals(test.getId(), 1);
    }

    @Test
    void testSetId() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        test.setId(2);
        assertEquals(test.getId(), 2);
    }

    @Test
    void testGetNbikes() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        assertEquals(test.getNbikes(),5);
    }

    @Test
    void testSetNbikes() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        test.setNbikes(6);
        assertEquals(test.getNbikes(), 6);
    }

    @Test
    void testAddBike() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        test.addBike();
        assertEquals(test.getNbikes(), 6);
    }

    @Test
    void testRemoveBike() {
        Building test = new Building("Aula", 7, 1, openingTime, closingTime);
        test.removeBike();
        assertEquals(test.getNbikes(), 6);
    }

    @Test
    void testEquals() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        Building test2 = new Building("Aula", 5, 1, openingTime, closingTime);
        assertEquals(test,test2);
    }

    @Test
    void testNotEquals() {
        Building test = new Building("Aula", 6, 1, openingTime, closingTime);
        Room test2 = new Room(1, "Star", "Aula", 1);
        assertNotEquals(test,test2);
    }

    @Test
    void testGetClosingTime() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        assertEquals(closingTime, test.getClosingTime());
    }

    @Test
    void testSetClosingTime() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        Time newTime = new Time(System.currentTimeMillis() + 5);
        test.setClosingTime(newTime);
        assertEquals(newTime, test.getClosingTime());
    }

    @Test
    void testGetOpeningTime() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        assertEquals(openingTime, test.getOpeningTime());
    }

    @Test
    void testSetOpeningTime() {
        Building test = new Building("Aula", 5, 1, openingTime, closingTime);
        Time newTime = new Time(System.currentTimeMillis() + 5);
        test.setOpeningTime(newTime);
        assertEquals(newTime, test.getOpeningTime());
    }
}
