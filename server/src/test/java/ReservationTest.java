import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.entities.Building;
import application.entities.Facility;
import application.entities.Reservation;
import application.entities.Room;
import application.entities.User;
import application.entities.UserType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ReservationTest {
    @Test
    void defaultConstructorTest() {
        Reservation test = new Reservation();
        assertNotNull(test);
    }

    @Test
    void reservationConstructorTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        assertEquals(testReservation.getRoom(), room);
    }

    @Test
    void reservationConstructor2Test() {
        Reservation test = new Reservation(1,
            Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), 2);
        assertNotNull(test);
    }

    @Test
    void reservationGetRoomIdTest() {
        Reservation test = new Reservation(1,
            Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), 2);
        assertEquals(test.getRoomId(), 1);
    }

    @Test
    void reservationGetUserId() {
        Reservation test = new Reservation(1,
            Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), 2);
        assertEquals(test.getUserId(),2);
    }

    @Test
    void reservationSetIdTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        testReservation.setId(10);
        assertEquals(testReservation.getId(), 10);
    }

    @Test
    void reservationGetRoomTest() {
        Building building = new Building("DWG",
            Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        assertEquals(testReservation.getRoom(), room);
    }

    @Test
    void reservationSetRoomTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        Room room1 = new Room(building, "b", 10, facilities, "roooooom");
        testReservation.setRoom(room1);
        assertEquals(testReservation.getRoom(), room1);
    }

    @Test
    void reservationGetStartTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        assertEquals(testReservation.getStart(), Timestamp.valueOf("2019-11-11 13:00:00"));
    }

    @Test
    void reservationSetStartTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        testReservation.setStart(Timestamp.valueOf("2019-11-11 12:00:00"));
        assertEquals(testReservation.getStart(), Timestamp.valueOf("2019-11-11 12:00:00"));
    }

    @Test
    void reservationGetEndTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        assertEquals(testReservation.getEnd(), Timestamp.valueOf("2019-11-11 15:00:00"));
    }

    @Test
    void reservationSetEndTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        testReservation.setEnd(Timestamp.valueOf("2019-11-11 16:00:00"));
        assertEquals(testReservation.getEnd(), Timestamp.valueOf("2019-11-11 16:00:00"));
    }

    @Test
    void reservationGetUserTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        assertEquals(testReservation.getUser(), user);
    }

    @Test
    void reservationSetUserTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        assertEquals(testReservation.getStart(), Timestamp.valueOf("2019-11-11 13:00:00"));
    }

    @Test
    void reservationEqualsTest() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "roooooom");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation testReservation1 = new Reservation(room,
            Timestamp.valueOf("2019-11-11 13:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        Reservation testReservation2 = new Reservation(room,
            Timestamp.valueOf("2019-11-11 10:00:00"),
            Timestamp.valueOf("2019-11-11 12:00:00"), user);
        testReservation1.setId(1);
        testReservation2.setId(2);
        assertFalse(testReservation1.equals(testReservation2));
    }

    @Test
    void reservationNotEqualsTest() {
        Reservation test = new Reservation(1,
            Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), 2);
        assertNotEquals(test, null);
    }
}
