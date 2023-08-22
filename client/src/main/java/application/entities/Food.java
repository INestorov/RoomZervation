package application.entities;

public class Food {

    private String name;
    private String description;
    private double price;
    private int restaurantId;
    private int id;
    private int amount;
    private String type;

    /**
     * Create a food instance.
     * @param name Name of the food
     * @param description Description of contents
     * @param price The price
     * @param restaurantId Id of the restaurant
     * @param id Id of the food in the database
     */
    public Food(String name, String description, double price,
                int restaurantId, int id, String type) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurantId = restaurantId;
        this.id = id;
        this.type = type;
    }

    /**
     * Create a food object to be displayed in the tabel in the myOrder scene.
     * @param name Name of the food.
     * @param description Description of the food.
     * @param price Price of the food.
     * @param amount Amount of times the food was ordered.
     */
    public Food(String name, String description, double price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
