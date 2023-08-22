import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.ReservationController;
import application.entities.Building;
import application.entities.Facility;
import application.entities.Reservation;
import application.entities.Room;
import application.entities.User;
import application.entities.UserType;
import application.repositories.ReservationRepository;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {
    @InjectMocks
    ReservationController reservationController;

    @Mock
    ReservationRepository reservationRepository;

    @Test
    public void testGetAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        Building building = new Building("DWG", Time.valueOf("10:00:00"),
            Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "nice");
        User user = new User(1, "a", "aa", UserType.Student,
            "23423", "someone@gmail.com");
        Reservation reservation1 = new Reservation(room, Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        Reservation reservation2 = new Reservation(room, Timestamp.valueOf("2020-01-01 11:00:00"),
            Timestamp.valueOf("2020-01-01 13:00:00"), user);
        reservations.add(reservation1);
        reservations.add(reservation2);
        when(reservationRepository.findAll()).thenReturn(reservations);
        List<Reservation> result = reservationController.getAllReservations().getBody();
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getRoom(), room);
    }

    @Test
    public void testGetReservationById() {
        Building building = new Building("DWG", Time.valueOf("10:00:00"),
            Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "nice");
        User user = new User(1, "a", "aa", UserType.Admin,
            "23423", "someone@gmail.com");
        Reservation reservation = new Reservation(room, Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        reservation.setId(0);
        when(reservationRepository.findById(0)).thenReturn(java.util.Optional.of(reservation));
        Reservation result = reservationController.getReservationById(0).getBody();
        assertEquals(result, reservation);
    }

    //    @Test
    //    public void testCreate() {
    //        List<Reservation> reservations = new ArrayList<>();
    //        Building building = new Building("DWG",
    //        Time.valueOf("10:00:00"), Time.valueOf("18:00:00"));
    //        Facility facility1 = new Facility("TV");
    //        Facility facility2 = new Facility("blackboard");
    //        Set<Facility> facilities,"nice = new HashSet<>();
    //        facilities,"nice.add(facility1);
    //        facilities,"nice.add(facility2);
    //        Room room = new Room(building, "a", 10,facilities,"nice);
    //        User user = new User("a", "aa", "aaa");
    //        Reservation reservation = new Reservation(room,
    //        Timestamp.valueOf("2019-11-11 14:00:00"),
    //            Timestamp.valueOf("2019-11-11 15:00:00"), user);
    //        reservations.add(reservation);
    //        when(reservationRepository.findAll()).thenReturn(reservations);
    //        List<Reservation> result = reservationController.create(reservation);
    //        assertEquals(result.get(0), reservation);
    //    }

    @Test
    public void testUpdateReservationById() {
        final List<Reservation> reservations = new ArrayList<>();
        Building building = new Building("DWG", Time.valueOf("10:00:00"),
            Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "nice");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation reservation = new Reservation(room, Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        reservation.setId(0);
        reservations.add(reservation);
        Reservation reservation1 = new Reservation(room, Timestamp.valueOf("2020-11-11 14:00:00"),
            Timestamp.valueOf("2020-11-11 15:00:00"), user);
        when(reservationRepository.findById(0)).thenReturn(java.util.Optional.of(reservation));
        when(reservationRepository.findAll()).thenReturn(reservations);
        List<Reservation> result = reservationController.updateReservationById(0, reservation1);
        assertEquals(result.get(0), reservation1);
    }

    @Test
    public void testDeleteUserById() {
        List<Reservation> reservations = new ArrayList<>();
        Building building = new Building("DWG", Time.valueOf("10:00:00"),
            Time.valueOf("18:00:00"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "a", 10, facilities, "nice");
        User user = new User(1, "a", "aa", UserType.Employee,
            "23423", "someone@gmail.com");
        Reservation reservation = new Reservation(room, Timestamp.valueOf("2019-11-11 14:00:00"),
            Timestamp.valueOf("2019-11-11 15:00:00"), user);
        reservations.add(reservation);
        when(reservationRepository.findById(0)).thenReturn(java.util.Optional.of(reservation));
        reservations.remove(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);
        List<Reservation> result = reservationController.deleteReservationById(0);
        assertEquals(result.size(), 0);
    }

    @Test
    public void testMakeReservation() {
        //            final List<Reservation> reservations = new ArrayList<>();
        //        Building building = new Building("DWG", Time.valueOf("10:00:00"),
        //        Time.valueOf("18:00:00"));
        //        Facility facility1 = new Facility("TV");
        //        Facility facility2 = new Facility("blackboard");
        //        Set<Facility> facilities,"nice = new HashSet<>();
        //        facilities,"nice.add(facility1);
        //        facilities,"nice.add(facility2);
        //        Room room = new Room(building, "a", 10, facilities,"nice);
        //        room.setId(0);
        //        User user = new User(1, "a", "aa", UserType.Admin);
        //        Reservation reservation = new Reservation(0,
        //        Timestamp.valueOf("2019-11-11 14:00:00"),
        //            Timestamp.valueOf("2019-11-11 15:00:00"), 1);
        //        reservations.add(reservation);
        //        when(reservationRepository.findAll()).thenReturn(reservations);
        //        reservationController.makeReservation(reservation);
        //        assertEquals(reservationRepository.findAll().get(0), reservation);
    }
}
