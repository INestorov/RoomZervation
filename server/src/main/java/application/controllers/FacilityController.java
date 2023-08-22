package application.controllers;

import application.entities.Facility;
import application.entities.Room;
import application.repositories.FacilityRepository;
import application.repositories.RoomRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facilities")
public class FacilityController {
    @Autowired
    FacilityRepository repository;
    @Autowired
    RoomRepository roomRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GET endpoint to find all facilities.
     *
     * @return a list of all facilties
     */
    @GetMapping("/read")
    public ResponseEntity<List<Facility>> getAllFacilities() {
        List<Facility> list = repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint to return a facility by id.
     *
     * @return one building
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<Facility> getFacilityById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Facility> facility = repository.findById(id);
        if (facility.isPresent()) {
            return new ResponseEntity<>(facility.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No Facilities exist");
        }
    }


    /**
     * POST EndPoint to create facility.
     */
    @PostMapping("/post")
    public Facility create(@RequestBody Map<Object, Object> req) {
        JsonNode jsonNode = objectMapper.valueToTree(req);
        String name = jsonNode.get("name").asText();
        Facility newEntity = new Facility();
        newEntity.setName(name);
        String arr = jsonNode.get("roomIds").toString();
        JsonArray jsonArray = JsonParser.parseString(arr).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Optional<Room> room = roomRepository.findById(jsonArray.get(i).getAsInt());
            if (room.isPresent()) {
                Room roomEntity = room.get();
                newEntity.getRooms().add(roomEntity);
                roomEntity.getFacilities().add(newEntity);
            }
        }
        repository.save(newEntity);
        for (int i = 0; i < jsonArray.size(); i++) {
            Optional<Room> room = roomRepository.findById(jsonArray.get(i).getAsInt());
            if (room.isPresent()) {
                Room roomEntity = room.get();
                roomEntity.getFacilities().add(newEntity);
                roomRepository.save(roomEntity);
            }
        }
        return newEntity;
    }

    /**
     * PUT Endpoint to create Facilities.
     *
     * @return buildings
     */
    @PutMapping("/put")
    public List<Facility> create(@RequestBody final Facility facility) {
        repository.save(facility);
        return repository.findAll();
    }

    /**
     * PUT endpoint to update facilities.
     *
     * @param id     facility id
     * @param entity facility object
     * @return list of all facilities
     * @throws IllegalArgumentException not facility
     */
    @PutMapping("update/{id}")
    public List<Facility> updateFacilityById(@PathVariable("id") Integer id,
                                             @RequestBody final Facility entity)
        throws IllegalArgumentException {
        Optional<Facility> facility = repository.findById(id);
        if (facility.isPresent()) {
            Facility newEntity = facility.get();

            newEntity.setName(entity.getName());

            repository.save(newEntity);

            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No facility exists");
        }
    }

    /**
     * DELETE Endpoint to delete Facility by id.
     */
    @DeleteMapping("delete/{id}")
    public List<Facility> deleteFacilityById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Facility> facility = repository.findById(id);

        if (facility.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No Facilities exist");
        }
    }
}
