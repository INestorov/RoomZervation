import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.FoodController;
import application.entities.Building;
import application.entities.Food;
import application.entities.Restaurant;
import application.repositories.FoodRepository;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)

public class FoodControllerTest {
    @InjectMocks
    FoodController foodController;

    @Mock
    FoodRepository foodRepository;

    private Building building = new Building("DWG1",
        Time.valueOf("14:22:00"), Time.valueOf("18:01:17"));

    private Food food = new Food(new Restaurant("LeGood", Time.valueOf("14:25:00"),
        Time.valueOf("16:25:00"), building),
        "very nice food", 5, "MEAT", "gnocchi");

    private Food food2 = new Food(new Restaurant("LeGoodGood",
        Time.valueOf("16:25:00"),
        Time.valueOf("20:25:00"), building), "very bad food",
        100, "also vegan", "lentils soup");

    @Test
    public void testGetFoodById() {
        food.setId(0);
        when(foodRepository.findById(0)).thenReturn(java.util.Optional.of(food));
        Food result = foodController.getFoodById(0).getBody();
        assertEquals(result, food);
    }

    //    @Test
    //    public void testGetFoodByRestaurantIdAndType() {
    //        food.setId(0);
    //        food.getRestaurant().setId(0);
    //        food2.setId(1);
    //        food2.getRestaurant().setId(1);
    //
    //        List<Food> menus = new ArrayList<>();
    //        menus.add(food);
    //        menus.add(food2);
    //        when(foodRepository.findById(0)).thenReturn(java.util.Optional.of(food));
    //        List<Food> result = foodController.getFoodsByRestaurantIdAndType(0,"MEAT").getBody();
    //        assertEquals(result.get(0).getType(), "MEAT");
    //        when(foodRepository.findFoodByRestaurant_IdAndType(1,"also vegan")).thenReturn(menus);
    //        result = (List<Food>) foodController.getFoodById(1).getBody();
    //        assertEquals(result.get(0).getType(),"also vegan");
    //    }

    @Test
    public void testCreate() {
        List<Food> foods = new ArrayList<>();
        foods.add(food);
        when(foodRepository.findAll()).thenReturn(foods);
        List<Food> result = foodController.create(food);
        assertEquals(result.get(0), food);
    }

    @Test
    public void testUpdateFoodById() {
        List<Food> foods = new ArrayList<>();
        food.setId(0);
        foods.add(food);
        foods.add(food2);
        when(foodRepository.findById(0)).thenReturn(java.util.Optional.of(food));
        when(foodRepository.findAll()).thenReturn(foods);
        List<Food> result = foodController.updateFoodById(0, food2);
        assertEquals(result.get(0).getDescription(), "very bad food");
    }

    @Test
    public void testDeleteFoodById() {
        List<Food> foods = new ArrayList<>();
        foods.add(food);
        food2.setId(1);
        when(foodRepository.findById(0)).thenReturn(java.util.Optional.of(food));
        foods.remove(food);
        when(foodRepository.findAll()).thenReturn(foods);
        List<Food> result = foodController.deleteFoodById(0);
        assertEquals(result.size(), 0);
    }
}


