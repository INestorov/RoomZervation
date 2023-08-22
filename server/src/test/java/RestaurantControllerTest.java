import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.RestaurantController;
import application.entities.Building;
import application.entities.Restaurant;
import application.repositories.RestaurantRepository;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)

public class RestaurantControllerTest {
    @InjectMocks
    RestaurantController restaurantController;

    @Mock
    RestaurantRepository restaurantRepository;

    @Test
    public void testGetAllRestaurants() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Restaurant restaurant1 = new Restaurant("a",
            Time.valueOf("10:00:00"), Time.valueOf("20:00:00"), building);
        Restaurant restaurant2 = new Restaurant("b",
            Time.valueOf("10:00:00"), Time.valueOf("20:00:00"), building);
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant1);
        restaurants.add(restaurant2);
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        List<Restaurant> result = restaurantController.getAllRestaurants().getBody();
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), "a");
    }

    @Test
    public void testGetRestaurantById() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        Restaurant restaurant = new Restaurant("a",
            Time.valueOf("10:00:00"), Time.valueOf("20:00:00"), building);
        restaurant.setId(0);
        when(restaurantRepository.findById(0)).thenReturn(java.util.Optional.of(restaurant));
        Restaurant result = restaurantController.getRestaurantById(0).getBody();
        assertEquals(result, restaurant);
    }

    @Test
    public void testCreate() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = new Restaurant("a",
            Time.valueOf("10:00:00"), Time.valueOf("20:00:00"), building);
        restaurants.add(restaurant);
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        List<Restaurant> result = restaurantController.create(restaurant);
        assertEquals(result.get(0), restaurant);
    }

    @Test
    public void testUpdateRestaurantById() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = new Restaurant("a",
            Time.valueOf("10:00:00"), Time.valueOf("20:00:00"), building);
        restaurant.setId(0);
        restaurants.add(restaurant);
        Restaurant restaurant1 = new Restaurant("b",
            Time.valueOf("10:00:00"), Time.valueOf("20:00:00"), building);
        when(restaurantRepository.findById(0)).thenReturn(java.util.Optional.of(restaurant));
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        List<Restaurant> result = restaurantController.updateRestaurantById(0, restaurant1);
        assertEquals(result.get(0).getName(), "b");
    }

    @Test
    public void testDeleteRestaurantById() {
        Building building = new Building("DWG1",
            Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = new Restaurant("a",
            Time.valueOf("10:00:00"), Time.valueOf("20:00:00"), building);
        restaurant.setId(0);
        restaurants.add(restaurant);
        when(restaurantRepository.findById(0)).thenReturn(java.util.Optional.of(restaurant));
        restaurants.remove(restaurant);
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        List<Restaurant> result = restaurantController.deleteRestaurantById(0);
        assertEquals(result.size(), 0);
    }
}


