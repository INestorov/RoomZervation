package application.controllers;

import application.entities.Facility;
import application.entities.Room;
import application.repositories.FacilityRepository;
import application.repositories.RoomRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    RoomRepository repository;
    @Autowired
    FacilityRepository facilityRepository;

    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * filter method for rooms(filters using below params).
     *
     * @param minSize    minimum size
     * @param maxSize    maximum size
     * @param buildingId id of building
     * @param facilityId id of facility to look for
     * @return a list of rooms
     */

    @GetMapping(value = {"/read/{minSize}/{maxSize}/{buildingId}/{facilityId}",
                         "/read/{minSize}/{maxSize}",
                         "/read/{minSize}/{maxSize}/{buildingId}"})
    public ResponseEntity<List<Room>> filter(@PathVariable Integer minSize,
                                             @PathVariable Integer maxSize,
                                             @PathVariable(required = false) Integer buildingId,
                                             @PathVariable(required = false) Integer facilityId) {

        List<Room> list;

        if (buildingId != null && facilityId != null) {
            list = repository.findRoomsByFacilities_IdAndSizeBetweenAndBuildingId(
                facilityId, minSize, maxSize, buildingId);
        } else if (buildingId == null && facilityId != null) {
            list = repository.findRoomsByFacilities_IdAndSizeBetween(
                facilityId, minSize, maxSize);
        } else if (buildingId != null && facilityId == null) {
            list = repository.findRoomsByBuildingIdAndSizeBetween(buildingId, minSize, maxSize);
        } else {
            list = repository.findRoomsBySizeBetween(minSize, maxSize);
        }

        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No rooms satisfying the conditions exist");
        }

    }

    /**
     * find a room by its id.
     *
     * @param id unique id of room
     * @return a room
     * @throws IllegalArgumentException in case of no existing rooms
     */
    @Cascade(value = CascadeType.ALL)
    @GetMapping("/read1/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {

        Optional<Room> room = repository.findById(id);

        if (room.isPresent()) {
            return new ResponseEntity<>(room.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No rooms exist");
        }

    }

    /**
     * Get facilities of a room by room_id.
     *
     * @param id unique id of room
     * @return facilities of a room
     * @throws IllegalArgumentException in case of no existing facilities
     */
    @GetMapping("/read/facilities/{id}")
    public ResponseEntity<Set<Facility>> getFacilitiesByRoomId(@PathVariable("id") Integer id)
        throws IllegalArgumentException {

        Optional<Room> room = repository.findById(id);

        if (room.isPresent()) {
            Set<Facility> list = room.get().getFacilities();

            if (list.size() > 0) {
                return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                throw new IllegalArgumentException("No facilities existing for the given room");
            }

        } else {
            throw new IllegalArgumentException("No rooms exist");
        }

    }

    /**
     * PUT endpoint to update rooms.
     *
     * @param id     room id
     * @param entity room entity to be updated
     * @return all rooms
     * @throws IllegalArgumentException invalid id
     */
    @PutMapping("/update/{id}")
    public List<Room> updateRoomById(@PathVariable("id") Integer id, @RequestBody final Room entity)
        throws IllegalArgumentException {
        Optional<Room> room = repository.findById(id);
        if (room.isPresent()) {
            Room newEntity = room.get();
            newEntity.setName(entity.getName());
            newEntity.setSize(entity.getSize());
            newEntity.setBuilding(entity.getBuilding());
            newEntity.setFacilities(entity.getFacilities());
            newEntity.setDescription(entity.getDescription());

            repository.save(newEntity);

            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No building exists");
        }
    }


    /**
     * DELETE Endpoint to delete room by id.
     */
    @Cascade(CascadeType.DELETE)
    @DeleteMapping("/delete/{id}")
    public List<Room> deleteRoomById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Room> room = repository.findById(id);

        if (room.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No rooms exist");
        }
    }

    /**
     * POST Endpoint to create Rooms.
     */
    @PostMapping("/post")
    public void makeRoom(@RequestBody final Room room) {
        try {
            System.out.println(room.getBuilding().getId() + " | " + room.getBuilding().getName());
            repository.createRoom(room.getName(), room.getSize(), room.getBuilding().getId());
        } catch (Exception e) {
            System.out.print("failed to make room");
        }
    }

    /**
     * add a facility to a room.
     *
     * @param facility the facility to add
     * @param roomId   the room to be updated
     * @throws IllegalArgumentException no room exists with that id
     */
    @PostMapping("/add/facility")
    public Optional<Room> addFacility(@RequestBody final Facility facility,
                                      @RequestParam("roomId") Integer roomId)
        throws IllegalArgumentException {
        Optional<Room> room = repository.findById(roomId);
        if (room.isPresent()) {
            Room newEntity = room.get();
            newEntity.addFacility(facility);
            repository.save(newEntity);
            return repository.findById(roomId);
        } else {
            throw new IllegalArgumentException("No room found");
        }
    }

    /**
     * DELETE Endpoint to delete a facility from a room.
     */
    @Cascade(CascadeType.DELETE)
    @PostMapping("/delete/facility")
    public Optional<Room> deleteFacilityFromRoom(@RequestBody Map<Object, Object> req)
        throws IllegalArgumentException {
        JsonNode jsonNode = objectMapper.valueToTree(req);
        int id = jsonNode.get("id").asInt();
        Optional<Facility> facility = facilityRepository.findById(
            jsonNode.get("facilityId").asInt());
        Optional<Room> room = repository.findById(id);
        if (room.isPresent() && facility.isPresent()) {
            Room newEntity = room.get();
            Facility facilityEntity = facility.get();
            if (newEntity.getFacilities().size() > 0) {
                newEntity.getFacilities().remove(facilityEntity);
                repository.save(newEntity);
                return repository.findById(id);
            } else {
                throw new IllegalArgumentException("room has no such facility");
            }
        } else {
            throw new IllegalArgumentException("No rooms exist");
        }
    }

    /**
     * Get Endpoint to get a picture of a room.
     *
     * @param roomId - id of the given room
     * @return picture of a room
     * @throws IOException =  throws an exception if a room isn't found
     */
    @GetMapping(value = "/image/{roomId}",
        produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getImage(@PathVariable("roomId") int roomId)
        throws IOException {

        ClassPathResource resource = new ClassPathResource("images/" + roomId + ".jpg");

        if (resource.exists()) {
            return ResponseEntity
                .ok()
                .body(new InputStreamResource(resource.getInputStream()));
        } else {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No picture found");
        }

    }
}
