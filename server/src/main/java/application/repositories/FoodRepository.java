package application.repositories;

import application.entities.Food;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findFoodByRestaurant_Id(Integer id);

    List<Food> findFoodByRestaurant_IdAndType(Integer id, String type);
}
