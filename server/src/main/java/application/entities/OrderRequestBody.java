package application.entities;

import java.sql.Timestamp;
import java.util.List;

public class OrderRequestBody {

    private int reservationId;
    private int restaurantId;
    private Timestamp date;
    private List<Integer> foodIds;

    /**
     * Create an OrderRequestBody instance to parse json string from the client.
     *
     * @param reservationId Id of the reservation received from the client.
     * @param restaurantId  Id of the restaurant where the food will be ordered.
     * @param date          When the order was issued.
     * @param foodIds       Ids of the foods in the order.
     */
    public OrderRequestBody(int reservationId, int restaurantId,
                            Timestamp date, List<Integer> foodIds) {
        this.reservationId = reservationId;
        this.restaurantId = restaurantId;
        this.date = date;
        this.foodIds = foodIds;
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Integer> getFoodIds() {
        return foodIds;
    }

    public void setFoodIds(List<Integer> foodIds) {
        this.foodIds = foodIds;
    }
}
