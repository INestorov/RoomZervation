package application.repositories;

import application.entities.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface RoomRepository extends JpaRepository<Room, Integer> {


    List<Room> findRoomsBySizeBetween(Integer min, Integer max);

    List<Room> findRoomsByBuildingIdAndSizeBetween(Integer buildingId, Integer min, Integer max);

    List<Room> findRoomsByFacilities_IdAndSizeBetween(Integer facilityId,
                                                      Integer minSize, Integer maxSize);

    List<Room> findRoomsByFacilities_IdAndSizeBetweenAndBuildingId(
        Integer facilityId, Integer min, Integer max, Integer buildingId);

    @Modifying
    @Query(value = "insert into room (name,size,building_id) VALUES"
        + " (:roomName,:roomSize,:buildingId)", nativeQuery = true)
    @Transactional
    void createRoom(@Param("roomName") String roomName,
                    @Param("roomSize") Integer size,
                    @Param("buildingId") Integer buildingId);

}
