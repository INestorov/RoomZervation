package application.repositories;

import application.entities.Bike;
import application.entities.BikeRental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BikeRepository extends JpaRepository<Bike, Integer> {
    List<Bike> findBikesByBuilding_Id(int id);

    Optional<Bike> findBikeByUser_Id(int id);

    List<Bike> findAllByOrderByBuilding();

    List<Bike> findBikesByBuildingIsNull();

    List<Bike> findBikesByBuildingIsNotNull();

    @Query(value = "SELECT * from bike b where b.building_id=:buildingId "
        + "and (b.reserved_until < CURRENT_TIMESTAMP "
        + "OR b.reserved_until is null)", nativeQuery = true)
    List<Bike> findAvailableBikesByBuilding_Id(int buildingId);
}

