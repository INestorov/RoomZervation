package application.controllers;

import application.entities.Restaurant;
import application.repositories.RestaurantRepository;
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
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    RestaurantRepository repository;

    /**
     * GET endpoint for reading all restaurants.
     *
     * @return a list of all restaurants
     */
    @GetMapping("/read")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() throws IllegalArgumentException {
        List<Restaurant> list = repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No any restaurant exists.");
        }
    }

    /**
     * GET endpoint for reading a specific restaurant.
     *
     * @return one http response containing a restaurant
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Restaurant> restaurant = repository.findById(id);
        if (restaurant.isPresent()) {
            return new ResponseEntity<>(restaurant.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No such restaurant exists");
        }
    }

    /**
     * POST Endpoint to create restaurants.
     *
     * @return restaurants
     */
    @PostMapping("/post")
    public List<Restaurant> create(@RequestBody final Restaurant restaurant) {
        repository.save(restaurant);
        return repository.findAll();
    }


    /**
     * PUT Endpoint to update restaurants.
     *
     * @return restaurants
     */
    @PutMapping("update/{id}")
    public List<Restaurant> updateRestaurantById(@PathVariable("id") Integer id,
                                                 @RequestBody final Restaurant entity)
        throws IllegalArgumentException {
        Optional<Restaurant> restaurant = repository.findById(id);
        if (restaurant.isPresent()) {
            Restaurant newEntity = restaurant.get();
            newEntity.setName(entity.getName());
            newEntity.setOpeningTime(entity.getOpeningTime());
            newEntity.setClosingTime(entity.getClosingTime());
            newEntity.setBuilding(entity.getBuilding());
            repository.save(newEntity);

            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No such restaurant exists");
        }
    }

    /**
     * DELETE Endpoint to delete restaurants by id.
     *
     * @return restaurants
     */
    @Cascade(CascadeType.ALL)
    @DeleteMapping("delete/{id}")
    public List<Restaurant> deleteRestaurantById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Restaurant> restaurant = repository.findById(id);

        if (restaurant.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No such restaurant exists");
        }
    }


}
