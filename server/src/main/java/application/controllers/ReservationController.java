package application.controllers;

//import static application.controllers.MailSenderController.mailSenderController;

import static javax.management.timer.Timer.ONE_HOUR;

import application.entities.Reservation;
import application.entities.UserType;
import application.repositories.OrderRepository;
import application.repositories.ReservationRepository;
import application.repositories.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAsync
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    ReservationRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    /** Scheduled method for deleting old reservations.
     *
     */
    @Async
    @Cascade(CascadeType.DELETE)
    @Scheduled(fixedRate = ONE_HOUR * 3)
    public void reservationCleanUp() {
        List<Reservation> list = repository.findSorted();
        for (Reservation r : list) {
            if (r.getStart().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {
                System.out.println("deleted old reservation");
                repository.delete(r);
            } else {
                break;
            }
        }
    }


    /**
     * find all reservations.
     *
     * @return sorted list of reservations (with respect to start time)
     */
    @GetMapping("/read/sorted")
    public ResponseEntity<List<Reservation>> getAllSorted() {

        List<Reservation> list = repository.findReservationByOrderByStartAsc();

        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint to read all reservations.
     *
     * @return all reservations
     */
    @GetMapping("/read")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> list = repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint to read a reservation by its id.
     *
     * @return a reservation
     */
    @GetMapping("/read/id/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Reservation> reservation = repository.findById(id);
        if (reservation.isPresent()) {
            return new ResponseEntity<>(reservation.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No Reservations exist");
        }
    }

    //    @PostMapping("/post")
    //    public List<Reservation> create(@RequestBody final Reservation reservation) {
    //        repository.save(reservation);
    //        return repository.findAll();
    //    }

    /**
     * PUT endpoint to update reservations.
     *
     * @param id     reservation id
     * @param entity reservation entity
     * @return list of all reservations
     * @throws IllegalArgumentException in case of bad input
     */
    @PutMapping("update/{id}")
    public List<Reservation> updateReservationById(
        @PathVariable("id") Integer id, @RequestBody final Reservation entity)
        throws IllegalArgumentException {
        Optional<Reservation> reservation = repository.findById(id);
        if (reservation.isPresent()) {
            Reservation newEntity = reservation.get();

            newEntity.setEnd(entity.getEnd());
            newEntity.setRoom(entity.getRoom());
            newEntity.setStart(entity.getStart());
            newEntity.setUser(entity.getUser());

            repository.save(newEntity);

            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No reservation exists");
        }
    }

    /**
     * DELETE Endpoint to delete Reservation by id.
     */
    @DeleteMapping("delete/{id}")
    public List<Reservation> deleteReservationById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Reservation> reservation = repository.findById(id);

        if (reservation.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No Reservations exist");
        }
    }

    /**
     * checks if user can overwrite reservations.
     *
     * @param reservation reservation to be saved.
     * @return true or false
     */
    @PostMapping("/post")
    public ResponseEntity<Boolean> reservationChecker(@RequestBody final Reservation reservation) {
        if (userRepository.findById(reservation.getUserId()).get().getType()
            .equals(UserType.Student)) {
            return makeReservation(reservation);
        } else {
            List<Reservation> list = repository.findReservationsByRoom_Id(
                reservation.getRoomId());
            if (list == null) {
                return makeReservation(reservation);
            }
            for (Reservation r : list) {
                if ((r.getStart().compareTo(reservation.getStart()) >= 0
                    && r.getEnd().compareTo(reservation.getEnd()) <= 0)
                    || (r.getStart().compareTo(reservation.getStart()) <= 0
                    && r.getEnd().compareTo(reservation.getStart()) >= 0)
                    || (r.getStart().compareTo(reservation.getStart()) >= 0
                    && r.getStart().compareTo(reservation.getEnd()) <= 0)) {
                    if (!r.getUser().getType().equals(UserType.Employee)) {
                        deleteReservationById(r.getId());
                        MailSenderController mailSenderController = new MailSenderController();
                        mailSenderController.sendExcuse(
                            r.getUser().getMail(),
                            r.getStart().toString(), r.getRoom().getName());
                    } else {
                        System.out.print("failed to make reservation");
                        return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.OK);
                    }
                }
            }
            return makeReservation(reservation);
        }
    }

    /**
     * POST Endpoint to create Reservations.
     */
    public ResponseEntity<Boolean> makeReservation(Reservation reservation) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis() + ((120 * 60) * 1000));
        List<Reservation> list = repository.findReservationsByUser_Id(reservation
            .getUserId(), currentTime);
        try {
            if (!list.isEmpty() && list.get(0).getUser().getType().equals(UserType.Student)) {
                int count = 0;
                for (Reservation r : list) {
                    if (r.getStart().toLocalDateTime().getDayOfYear()
                        == reservation.getStart().toLocalDateTime().getDayOfYear()) {
                        count++;
                    }
                }
                if (count < 2 && list.size() < 5) {
                    repository.createReservation(reservation.getRoomId(),
                        reservation.getStart(),
                        reservation.getEnd(), reservation.getUserId());
                    return new ResponseEntity<>(true, new HttpHeaders(), HttpStatus.OK);
                } else {
                    System.out.println("COUNT " + count + " SIZE " + list.size());
                    return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.OK);
                }
            } else {
                repository.createReservation(reservation.getRoomId(),
                    reservation.getStart(), reservation.getEnd(),
                    reservation.getUserId());
                return new ResponseEntity<>(true, new HttpHeaders(), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.print("failed to make reservation");
            return new ResponseEntity<>(false, new HttpHeaders(), HttpStatus.OK);
        }
    }


    /**
     * return all reservations based on username.
     *
     * @param username name of user
     * @return reservations
     */
    @GetMapping("/read1/{username}")
    public ResponseEntity<List<Reservation>> getReservationByUsername(
        @PathVariable("username") String username)
        throws IllegalArgumentException {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis() + ((120 * 60) * 1000));
        List<Reservation> list = repository.findReservationsByUsername(username, currentTime);
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }

    }

}
