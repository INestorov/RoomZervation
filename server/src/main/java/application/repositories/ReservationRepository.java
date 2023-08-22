package application.repositories;

import application.entities.Reservation;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findReservationByOrderByStartAsc();

    @Query("select r from Reservation r order by r.start")
    List<Reservation> findSorted();

    @Modifying
    @Query(value = "insert into reservation (room_id,start,end,user_id) VALUES"
        + " (:roomId,:startTime,:endTime,:userId)", nativeQuery = true)
    @Transactional
    void createReservation(@Param("roomId") Integer roomId, @Param("startTime") Timestamp startTime,
                           @Param("endTime") Timestamp endTime, @Param("userId") Integer userId);

    @Query("select  r from Reservation r where r.user.username = :username and r.end > :current ")
    List<Reservation> findReservationsByUsername(String username, Timestamp current);

    @Query("select  r from Reservation r where r.user.id = :userid and r.end > :current ")
    List<Reservation> findReservationsByUser_Id(int userid, Timestamp current);

    List<Reservation> findReservationsByRoom_Id(int room);

}
