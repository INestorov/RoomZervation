import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Room;
import application.entities.TimeSlot;
import java.sql.Timestamp;
import org.junit.jupiter.api.Test;

public class TimeSlotTest {

    @Test
    void testConstructor() {
        TimeSlot test = new TimeSlot(null,1,"13:00", "15:00");
        assertNotNull(test);
    }

    @Test
    void testConstructor2() {
        TimeSlot test = new TimeSlot(1, "13:00", "15:00");
        assertNotNull(test);
    }

    @Test
    void testDefaultConstructor() {
        TimeSlot test = new TimeSlot();
        assertNotNull(test);
    }

    @Test
    void testConstructor3() {
        TimeSlot test = new TimeSlot(1, Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"));
        assertNotNull(test);
    }

    @Test
    void testConstructor4() {
        TimeSlot test = new TimeSlot(1, "11:00", "12:00", 1);
        assertNotNull(test);
    }

    @Test
    void testConstructor5() {
        Room room = new Room(1, "Star", "Aula", 1);
        TimeSlot test = new TimeSlot(room,1, "11:00", "12:00", 1);
        assertNotNull(test);
    }

    @Test
    void testGetRoom() {
        Room room = new Room(1, "Star", "Aula", 1);
        TimeSlot test = new TimeSlot(room,1,"13:00", "15:00");
        assertEquals(test.getRoom(), room);
    }

    @Test
    void testGetEndTime() {
        TimeSlot test = new TimeSlot(1, Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"));
        assertEquals(test.getEndTime(), Timestamp.valueOf("2019-11-11 15:00:00"));
    }

    @Test
    void testGetId() {
        TimeSlot test = new TimeSlot(1, "13:00", "15:00");
        assertEquals(test.getId(), 1);
    }

    @Test
    void testGetStart() {
        TimeSlot test = new TimeSlot(1, "13:00", "15:00");
        assertEquals(test.getStart(), "13:00");
    }

    @Test
    void testGetEnd() {
        TimeSlot test = new TimeSlot(1, "13:00", "15:00");
        assertEquals(test.getEnd(), "15:00");
    }

    @Test
    void testToString() {
        Room room = new Room(1, "Star", "Aula", 1);
        TimeSlot test = new TimeSlot(room,1,"13:00", "15:00");
        assertEquals(test.toString(), "IntegerProperty [value: 1] StringProperty [value: Star] "
            + "StringProperty [value: Aula] IntegerProperty [value: 1] 13:00 15:00");
    }

    @Test
    void testGetReservationId() {
        TimeSlot test = new TimeSlot(1, "11:00", "12:00", 1);
        assertEquals(test.getReservationId(), 1);
    }

    @Test
    void testSetReservationId() {
        TimeSlot test = new TimeSlot(1, "11:00", "12:00", 1);
        test.setReservationId(2);
        assertEquals(test.getReservationId(), 2);
    }
}
