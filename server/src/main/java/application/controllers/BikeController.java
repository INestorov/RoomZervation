package application.controllers;

import application.entities.Bike;
import application.entities.BikeRental;
import application.entities.Building;
import application.entities.User;
import application.repositories.BikeRentalRepository;
import application.repositories.BikeRepository;
import application.repositories.BuildingRepository;
import application.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
@RequestMapping("/bikes")
public class BikeController {

    @Autowired
    BikeRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    BikeRentalRepository bikeRentalRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GET all bikes.
     *
     * @return a list of all bikes
     * @throws IllegalArgumentException no bikes
     */
    @GetMapping("/read/all")
    public ResponseEntity<List<Bike>> getAllBikes() throws IllegalArgumentException {
        List<Bike> list = repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No bike exists");
        }
    }

    /**
     * GET all bikes order by building for admins to check the bikes.
     *
     * @return a list of all bikes order by building
     */
    @GetMapping("/read")
    public ResponseEntity<List<Bike>> getAllBikesOrderByBuilding() throws IllegalArgumentException {
        List<Bike> list = repository.findAllByOrderByBuilding();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No bike exists");
        }
    }

    /**
     * GET endpoint for reading a specific bike.
     *
     * @return one http response containing a bike
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<Bike> getBikeById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Bike> bike = repository.findById(id);
        if (bike.isPresent()) {
            return new ResponseEntity<>(bike.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No such bike exists");
        }
    }

    /**
     * GET a list of available bikes at a specific building for admins to check the bikes.
     *
     * @return one http response containing the list
     */
    @GetMapping("/read/building/{building_id}")
    public ResponseEntity<List<Bike>> getBikesByBuildingId(@PathVariable("building_id") Integer id)
        throws IllegalArgumentException {
        List<Bike> list = repository.findAvailableBikesByBuilding_Id(id);
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No available bike exists");
        }
    }

    /**
     * GET the number of available bikes at a specific building.
     *
     * @return the number of available bikes at a specific building
     */
    @GetMapping("/read/number/{building_id}")
    public int getAvailableBikesNumberByBuildingId(@PathVariable("building_id") Integer id) {
        List<Bike> list = repository.findAvailableBikesByBuilding_Id(id);
        return list.size();
    }

    /**
     * GET a list of bikes which are not returned yet for admins.
     *
     * @return a list of bikes which are not returned yet.
     */
    @GetMapping("/read/notReturned")
    public ResponseEntity<List<Bike>> getBikesNotReturned() throws IllegalArgumentException {
        List<Bike> list = repository.findBikesByBuildingIsNull();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No bike exists");
        }
    }


    /**
     * GET a list of bikes which have a building.
     *
     * @return a list of bikes which have building.
     */
    @GetMapping("/read/withBuilding")
    public ResponseEntity<List<Bike>> getBikesWithBuilding() throws IllegalArgumentException {
        List<Bike> list = repository.findBikesByBuildingIsNotNull();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No bike exists");
        }
    }

    /**
     * GET true/false if user has bike in use.
     *
     * @param id UserId
     * @return if user has bike in Use (boolean)
     */
    @GetMapping("/read/isUser/{id}")
    public boolean isBikeInUse(@PathVariable("id") Integer id) {
        return repository.findBikeByUser_Id(id).isPresent();
    }

    /**
     * GET bike that is in user by user with given id.
     *
     * @param userId UserId
     * @return bike in use by user with given id
     * @throws IllegalArgumentException if bike is not found
     */
    @GetMapping("/read/user/{userId}")
    public ResponseEntity<Bike> getBikeByUserId(
        @PathVariable("userId") Integer userId) throws IllegalArgumentException {
        Optional<Bike> bike = repository.findBikeByUser_Id(userId);
        if (bike.isPresent()) {
            return new ResponseEntity<>(bike.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No bike found for id: " + userId);
        }
    }

    /**
     * POST Endpoint to create a bike.
     *
     * @return all bikes
     */
    @PostMapping("/post")
    public List<Bike> create(@RequestBody final Bike bike) throws IllegalArgumentException {
        if (bike.getBuilding() == null) {
            throw new IllegalArgumentException("A bike should have a building attribute!");
        }
        repository.save(bike);
        return repository.findAll();
    }

    /**
     * PUT Endpoint to update a bike.
     *
     * @return all bikes
     */
    @PutMapping("update/{id}")
    public List<Bike> updateBikeById(@PathVariable("id") Integer id,
                                     @RequestBody final Bike entity)
        throws IllegalArgumentException {
        Optional<Bike> bike = repository.findById(id);
        if (bike.isPresent()) {
            Bike newEntity = bike.get();
            newEntity.setBuilding(entity.getBuilding());
            newEntity.setBikeRentals(entity.getBikeRentals());
            repository.save(newEntity);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No such bike exists");
        }
    }

    /**
     * Update a bike and a bike rental when a user returns the bike.
     *
     * @param requestBody JSON contains bike id, building id
     * @return the bike rental
     */
    @PostMapping("update/return")
    public BikeRental returnBike(@RequestBody Map<String, Object> requestBody)
        throws IllegalArgumentException {
        JsonNode jsonNode = objectMapper.valueToTree(requestBody);
        Optional<Bike> bike = repository.findById(jsonNode.get("bikeId").asInt());
        Optional<Building> building = buildingRepository
            .findById(jsonNode.get("buildingId").asInt());
        Bike newEntity = bike.get();
        Optional<BikeRental> bikeRental = bikeRentalRepository
            .findBikeRentalByUserAndEndIsNull(newEntity.getUser().getId());
        if (bike.isPresent() && building.isPresent()) {
            Building buildingEntity = building.get();
            // The bikeRental entity linked to should be updated
            BikeRental newBikeRentalEntity;
            if (bikeRental.isPresent()) {
                newBikeRentalEntity = bikeRental.get();
                newBikeRentalEntity.setEnd(new Timestamp(System.currentTimeMillis()
                    + ((120 * 60) * 1000)));
                bikeRentalRepository.save(newBikeRentalEntity);
            } else {
                throw new IllegalArgumentException("No such bike rental exists.");
            }
            newEntity.setBuilding(buildingEntity);
            newEntity.setUser(null);
            repository.save(newEntity);
            return newBikeRentalEntity;
        } else {
            throw new IllegalArgumentException("No such bike or building exists.");
        }
    }

    /**
     * Update a bike and set it to reserved.
     *
     * @param requestBody JSON contains bike id and user id
     * @return the bike.
     */
    @PostMapping("/rent")
    public Bike rentBike(@RequestBody Map<Object, Object> requestBody)
        throws IllegalArgumentException {
        JsonNode jsonNode = objectMapper.valueToTree(requestBody);
        int userId = jsonNode.get("userId").asInt();
        Optional<Bike> bike = repository.findById(jsonNode.get("bikeId").asInt());
        Optional<Bike> bikeInUse = repository.findBikeByUser_Id(userId);
        Optional<User> user = userRepository.findById(userId);
        if (bikeInUse.isPresent()) {
            throw new IllegalArgumentException("User is already renting a bike!");
        } else if (user.isPresent()) {
            if (bikeRentalRepository
                .findBikeRentalsByUserAndStartIsInToday(user.get().getId()).size() == 2) {
                throw new IllegalArgumentException("Has rented twice today!");
            } else if (bike.isPresent()) {
                Bike newEntity = bike.get();
                User userEntity = user.get();
                newEntity.setUser(userEntity);
                newEntity.setReservedUntil(new Timestamp(jsonNode.get("Date").asLong()
                    + TimeUnit.MINUTES.toMillis(5) + TimeUnit.HOURS.toMillis(2)));
                repository.save(newEntity);
                return newEntity;
            } else {
                throw new IllegalArgumentException("No such bike exists.");
            }
        } else {
            throw new IllegalArgumentException("No such user exists.");
        }
    }

    /**
     * Set building of reserved bike to null and create a new bikeRental.
     * @param req Request body (Json string)
     * @return the bike picked up.
     * @throws IllegalArgumentException In case the bike with userId is not found.
     */
    @PostMapping("/pickup")
    public Bike pickUpBike(@RequestBody Map<Object, Object> req) throws IllegalArgumentException {
        JsonNode jsonNode = objectMapper.valueToTree(req);
        Optional<Bike> bike = repository.findBikeByUser_Id(
            jsonNode.get("id").asInt());
        if (bike.isPresent()) {
            Bike newEntity = bike.get();
            newEntity.setBuilding(null);
            BikeRental bikeRental = new BikeRental(newEntity, newEntity.getUser(),
                new Timestamp(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2)), null);
            newEntity.getBikeRentals().add(bikeRental);
            bikeRentalRepository.save(bikeRental);
            repository.save(newEntity);
            return newEntity;
        }
        throw new IllegalArgumentException("No bike found for userId: " + jsonNode.get("id"));
    }

    /**
     * cancel the current reservation of given user.
     * @param req RequestBody (Json String)
     * @return the bike of canceled reservation.
     * @throws IllegalArgumentException in case the bike with userId is not found.
     */
    @PostMapping("/cancel")
    public Bike cancel(@RequestBody Map<Object, Object> req) throws IllegalArgumentException {
        JsonNode jsonNode = objectMapper.valueToTree(req);
        Optional<Bike> bike = repository.findBikeByUser_Id(
            jsonNode.get("id").asInt());
        if (bike.isPresent()) {
            Bike newEntity = bike.get();
            newEntity.setUser(null);
            newEntity.setReservedUntil(null);
            repository.save(newEntity);
            return newEntity;
        }
        throw new IllegalArgumentException("No bike found for userId: " + jsonNode.get("id"));
    }

    /**
     * Set user of bike of expired reservation to null.
     * @param req RequestBody (Json string)
     * @return the updated bike or a new bike if user is not found.
     */
    @PostMapping("/post/new")
    public Bike newReservation(@RequestBody Map<Object, Object> req) {
        JsonNode jsonNode = objectMapper.valueToTree(req);
        Optional<Bike> bike = repository.findBikeByUser_Id(
            jsonNode.get("id").asInt());
        if (bike.isPresent()) {
            Bike newEntity = bike.get();
            newEntity.setUser(null);
            repository.save(newEntity);
            return newEntity;
        } else {
            return new Bike();
        }
    }

    /**
     * DELETE Endpoint to delete bike by id.
     *
     * @return all bikes
     */
    @Cascade(CascadeType.ALL)
    @DeleteMapping("delete/{id}")
    public List<Bike> deleteBikeById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Bike> bike = repository.findById(id);

        if (bike.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No such bike exists");
        }
    }


}
