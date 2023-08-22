import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.RoomController;
import application.entities.Building;
import application.entities.Facility;
import application.entities.Room;
import application.repositories.FacilityRepository;
import application.repositories.RoomRepository;
import com.google.gson.JsonObject;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class RoomControllerTest {
    @InjectMocks
    RoomController roomController;

    @Mock
    RoomRepository roomRepository;

    @Mock
    FacilityRepository facilityRepository;

    @Test
    public void testFilter() {
        Building building = new Building("DWG1", Time.valueOf("14:22:00"),
            Time.valueOf("18:01:17"));
        building.setId(0);
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room1 = new Room(building, "aaa", 15, facilities, "nice");
        Room room2 = new Room(building, "bbb", 10, facilities, "nice");
        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        when(roomRepository.findRoomsByFacilities_IdAndSizeBetweenAndBuildingId(
            0, 8, 15, 0)).thenReturn(rooms);
        List<Room> result = roomController.filter(8, 15, 0, 0).getBody();
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), "aaa");
    }

    @Test
    public void testGetRoomById() {
        Building building = new Building("DWG1", Time.valueOf("14:22:00"),
            Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "abc", 10, facilities, "nice");
        room.setId(0);
        when(roomRepository.findById(0)).thenReturn(java.util.Optional.of(room));
        Room result = roomController.getRoomById(0).getBody();
        assertEquals(result, room);
    }

    @Test
    public void testCreate() {
        //        List<Room> rooms = new ArrayList<>();
        //        Building building = new Building("DWG1", Time.valueOf("14:22:00"),
        //            Time.valueOf("18:01:17"));
        //        Facility facility1 = new Facility("TV");
        //        Facility facility2 = new Facility("blackboard");
        //        Set<Facility> facilities = new HashSet<>();
        //        facilities.add(facility1);
        //        facilities.add(facility2);
        //        Room room = new Room(building, "abc", 10, facilities);
        //        rooms.add(room);
        //        when(roomRepository.findAll()).thenReturn(rooms);
        //        List<Room> result = roomController.create(room);
        //        assertEquals(result.get(0), room);
    }

    @Test
    public void testUpdateRoomById() {
        final List<Room> rooms = new ArrayList<>();
        Building building = new Building("DWG1", Time.valueOf("14:22:00"),
            Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "abc", 10, facilities, "nice");
        room.setId(0);
        rooms.add(room);
        Room room1 = new Room(building, "bcd", 12, facilities, "nice");
        when(roomRepository.findById(0)).thenReturn(java.util.Optional.of(room));
        when(roomRepository.findAll()).thenReturn(rooms);
        List<Room> result = roomController.updateRoomById(0, room1);
        assertEquals(result.get(0).getName(), "bcd");
    }

    @Test
    public void testDeleteRoomById() {
        final List<Room> rooms = new ArrayList<>();
        Building building = new Building("DWG1", Time.valueOf("14:22:00"),
            Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "abc", 10, facilities, "ncie");
        room.setId(0);
        rooms.add(room);
        when(roomRepository.findById(0)).thenReturn(java.util.Optional.of(room));
        rooms.remove(room);
        when(roomRepository.findAll()).thenReturn(rooms);
        List<Room> result = roomController.deleteRoomById(0);
        assertEquals(result.size(), 0);
    }

    @Test
    public void testAddAndRemoveFacility() {
        final List<Room> rooms = new ArrayList<>();
        Building building = new Building("DWG1", Time.valueOf("14:22:00"),
            Time.valueOf("18:01:17"));
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("blackboard");
        Set<Facility> facilities = new HashSet<>();
        facilities.add(facility1);
        facilities.add(facility2);
        Room room = new Room(building, "abc", 10, facilities, "nice");
        room.setId(0);
        when(roomRepository.findById(0)).thenReturn(java.util.Optional.of(room));
        Facility toAdd = new Facility("super wow facility");
        toAdd.setId(0);
        Optional<Room> result = roomController.addFacility(toAdd, 0);
        assertEquals(result.get().getFacilities().size(), 3);
        Map<Object, Object> map = new HashMap<>();
        map.put("id", 0);
        map.put("facilityId", 0);
        when(facilityRepository.findById(0)).thenReturn(java.util.Optional.of(toAdd));
        result = roomController.deleteFacilityFromRoom(map);
        assertEquals(result.get().getFacilities().size(), 2);
    }
}