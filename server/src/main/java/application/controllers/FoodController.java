package application.controllers;

import application.entities.Food;
import application.repositories.FoodRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menus")
public class FoodController {

    @Autowired
    FoodRepository repository;

    /**
     * GET endpoint for reading all menus of a restaurant.
     *
     * @param id   restaurant id
     * @param type food type
     * @return menus
     */
    @GetMapping("/read")
    public ResponseEntity<List<Food>> getFoodsByRestaurantIdAndType(
        @RequestParam(required = true) Integer id,
        @RequestParam(required = false) String type) {
        List<Food> list = repository.findFoodByRestaurant_Id(id);
        if (list.size() > 0) {
            if (type != null) {
                list = repository.findFoodByRestaurant_IdAndType(id, type);
            }
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    @GetMapping("/read/all/{restaurantId}")
    public ResponseEntity<List<Food>> getFoodsByRestaurantId(
        @PathVariable int restaurantId
    ) {
        List<Food> list = repository.findFoodByRestaurant_Id(restaurantId);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * GET endpoint for reading a specific food.
     *
     * @return one http response containing a food
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Food> menu = repository.findById(id);
        if (menu.isPresent()) {
            return new ResponseEntity<>(menu.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No menu exists");
        }
    }

    /**
     * POST Endpoint to create food.
     *
     * @return menus
     */
    @PostMapping("/post")
    public List<Food> create(@RequestBody final Food food) {
        repository.save(food);
        return repository.findAll();
    }


    /**
     * PUT Endpoint to update foods.
     *
     * @return menus
     */
    @PutMapping("update/{id}")
    public List<Food> updateFoodById(@PathVariable("id") Integer id,
                                     @RequestBody final Food entity)
        throws IllegalArgumentException {
        Optional<Food> menu = repository.findById(id);
        if (menu.isPresent()) {
            Food newEntity = menu.get();
            newEntity.setRestaurant(entity.getRestaurant());
            newEntity.setDescription(entity.getDescription());
            newEntity.setPrice(entity.getPrice());
            newEntity.setType(entity.getType());
            newEntity.setName(entity.getName());
            repository.save(newEntity);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No menu exists");
        }
    }

    /**
     * DELETE Endpoint to delete food by id.
     *
     * @return menus
     */
    @Cascade(CascadeType.ALL)
    @DeleteMapping("delete/{id}")
    public List<Food> deleteFoodById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Food> menu = repository.findById(id);
        if (menu.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No menu exists");
        }
    }


}


