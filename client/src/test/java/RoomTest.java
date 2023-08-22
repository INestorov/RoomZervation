import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Room;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.Test;

public class RoomTest {

    @Test
    void testConstructor() {
        Room test = new Room(1, "Star", "Aula", 1);
        assertNotNull(test);
    }

    @Test
    void testConstructor2() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertNotNull(test);
    }

    @Test
    void testGetDescription() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertEquals(test.getDescription(), "nice");
    }

    @Test
    void testDescriptionProperty() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertEquals(test.descriptionProperty().get(), new SimpleStringProperty("nice").get());
    }

    @Test
    void testGetFacilities() {
        List<String> facilities = new ArrayList<>();
        facilities.add("whiteboard");
        facilities.add("projector");
        Room test = new Room(1, "Star", 1, 1, facilities, "ncie");
        assertEquals(test.getFacilities(), "whiteboard, projector");
    }

    @Test
    void testGetNoFacilities() {
        List<String> facilities = new ArrayList<>();
        Room test = new Room(1, "Star", 1, 1, facilities, "ncie");
        assertEquals(test.getFacilities(), "Facilities not uploaded yet.");
    }

    @Test
    void testGetId() {
        Room test = new Room(1, "Star", "Aula", 1);
        assertEquals(test.getId(), 1);
    }

    @Test
    void testGetBuildingId() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertEquals(test.getBuildingId(), 1);
    }

    @Test
    void testIdProperty() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertEquals(test.idProperty().get(), new SimpleIntegerProperty(1).get());
    }

    @Test
    void testGetName() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertEquals(test.getName(), "Star");
    }

    @Test
    void testSetName() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        test.setName("Ocean");
        assertEquals(test.getName(), "Ocean");
    }

    @Test
    void testNameProperty() {
        Room test = new Room(1, "Star", "Aula", 1);
        assertEquals(test.nameProperty().get(), new SimpleStringProperty("Star").get());
    }

    @Test
    void testGetSize() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertEquals(test.getSize(),  1);
    }

    @Test
    void testSetSize() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        test.setSize(2);
        assertEquals(test.getSize(),  2);
    }

    @Test
    void testSizeProperty() {
        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertEquals(test.sizeProperty().get(), new SimpleIntegerProperty(1).get());
    }

    @Test
    void testGetBuildingName() {
        Room test = new Room(1, "Star", "Aula", 1);
        assertEquals(test.getBuildingName(), "Aula");
    }

    @Test
    void testGetBuilding() {
        //test with wiremock
    }

    @Test
    void testBuildingNameProperty() {
        Room test = new Room(1, "Star", "Aula", 1);
        assertEquals(test.buildingNameProperty().get(), new SimpleStringProperty("Aula").get());
    }

    @Test
    void testToString() {
        Room test = new Room(1, "Star", "Aula", 1);
        assertEquals(test.toString(), "IntegerProperty [value: 1] StringProperty [value: Star] "
            + "StringProperty [value: Aula] IntegerProperty [value: 1] ");
    }
}
