package application.repositories;

import application.entities.BikeRental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BikeRentalRepository extends JpaRepository<BikeRental, Integer> {
    List<BikeRental> findBikeRentalsByBike_Id(int id);

    @Query(value = "SELECT * from bike_rental b  "
        + "where b.user_id=:userId and DATE(b.start)=current_date()",nativeQuery = true)
    List<BikeRental> findBikeRentalsByUserAndStartIsInToday(@Param("userId") int userId);

    @Query(value = "SELECT * from bike_rental b "
        + "where b.user_id=:userId and b.end is NULL", nativeQuery = true)
    Optional<BikeRental> findBikeRentalByUserAndEndIsNull(@Param("userId") int userId);

    @Query(value = "SELECT * from bike_rental b where TIMESTAMPDIFF"
        + "(SECOND, b.start, COALESCE(b.end,CURRENT_TIMESTAMP ))>=14400", nativeQuery = true)
    List<BikeRental> findLateBikeRentals();

    List<BikeRental> findBikeRentalsByEndIsNull();
}
