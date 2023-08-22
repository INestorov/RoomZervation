package application.entities;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int reservationId;
    private int restaurantId;
    private long date;
    private List<Integer> foodIds;

    /**
     * Create an order instance.
     *
     * @param reservationId Id of the reservation where the food will be delivered
     * @param restaurantId Id of the restaurant where the food will be ordered
     * @param date Time the food was ordered
     */
    public Order(int reservationId, int restaurantId, long date) {
        this.reservationId = reservationId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.foodIds = new ArrayList<>();
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<Integer> getFoodIds() {
        return foodIds;
    }

    public void setFoodIds(List<Integer> foodIds) {
        this.foodIds = foodIds;
    }
}
