package application.entities;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    private List<Food> foods;
    private int restaurantId;

    /**
     * Create a Basket instance.
     * @param restaurantId Id of the restaurant where the order will be placed.
     */
    public Basket(int restaurantId) {
        this.restaurantId = restaurantId;
        this.foods = new ArrayList<Food>();
    }

    public void add(Food food) {
        foods.add(food);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public int getSize() {
        return foods.size();
    }

    public Food getFood(int i) {
        return foods.get(i);
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    /**
     * Check if basket is empty.
     * @return true if basket is empty.
     */
    public boolean isEmpty() {
        if (foods.size() == 0) {
            return true;
        }
        return false;
    }
}
