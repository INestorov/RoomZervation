package application.controllers;

import application.entities.BikeRental;
import application.repositories.BikeRentalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/bikeRentals")
public class BikeRentalController {

    @Autowired
    BikeRentalRepository repository;

    /**
     * GET all bike rentals.
     *
     * @return a list of all bike rentals
     */
    @GetMapping("/read")
    public ResponseEntity<List<BikeRental>> getAllBikeRentals() {
        List<BikeRental> list = repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET bikeRental of user with given id.
     */
    @GetMapping("read/user/{id}")
    public ResponseEntity<BikeRental> getBikeRentalByUser_Id(
        @PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<BikeRental> bikeRental = repository.findBikeRentalByUserAndEndIsNull(id);
        if (bikeRental.isPresent()) {
            return new ResponseEntity<>(bikeRental.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No such bike rental exists");
        }
    }

    /**
     * GET endpoint for reading a specific bike rental.
     *
     * @return one http response containing a bike rental
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<BikeRental> getBikeRentalById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<BikeRental> bikeRental = repository.findById(id);
        if (bikeRental.isPresent()) {
            return new ResponseEntity<>(bikeRental.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No such bike rental exists");
        }
    }

    /**
     * GET endpoint for reading bike rentals of a bike.
     *
     * @return one http response containing bike rentals of a bike
     */
    @GetMapping("/read/byBike/{bike_id}")
    public ResponseEntity<List<BikeRental>> getBikeRentalsByBikeId(
        @PathVariable("bike_id") Integer id) {
        List<BikeRental> list = repository.findBikeRentalsByBike_Id(id);
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint for reading bike rentals more than 4 hours.
     *
     * @return one http response containing bike rentals more than 4 hours.
     */
    @GetMapping("/read/late")
    public ResponseEntity<List<BikeRental>> getLateBikeRentals() {
        List<BikeRental> list = repository.findLateBikeRentals();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint for reading not returned bike rentals.
     *
     * @return one http response containing bike rentals not returned
     */
    @GetMapping("/read/notReturned")
    public ResponseEntity<List<BikeRental>> getNotReturnedBikeRentals() {
        List<BikeRental> list = repository.findBikeRentalsByEndIsNull();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * POST Endpoint to create a bike rental.
     *
     * @return all bike rentals
     */
    @PostMapping("/post")
    public List<BikeRental> create(@RequestBody final BikeRental bikeRental)
        throws IllegalArgumentException {
        repository.save(bikeRental);
        return repository.findAll();
    }

    /**
     * Update a bike rental by Id.
     *
     * @param id     id
     * @param entity entity
     * @return the list of BikeRentals
     */

    @PutMapping("update/{id}")
    public List<BikeRental> updateBikeRentalById(@PathVariable("id") Integer id,
                                                 @RequestBody final BikeRental entity)
        throws IllegalArgumentException {
        Optional<BikeRental> bikeRental = repository.findById(id);
        if (bikeRental.isPresent()) {
            BikeRental newEntity = bikeRental.get();
            newEntity.setBike(entity.getBike());
            newEntity.setStart(entity.getStart());
            newEntity.setEnd(entity.getEnd());
            newEntity.setUser(entity.getUser());
            repository.save(newEntity);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No such bike rental exists");
        }
    }


    /**
     * DELETE Endpoint to delete bike rental by id.
     *
     * @return all bike rentals
     */
    @Cascade(CascadeType.ALL)
    @DeleteMapping("delete/{id}")
    public List<BikeRental> deleteBikeRentalById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<BikeRental> bikeRental = repository.findById(id);

        if (bikeRental.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No such bike rental exists");
        }
    }

}
