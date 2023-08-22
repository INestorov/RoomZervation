package application.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @ManyToOne
    @NotNull
    @JoinColumn(name = "restaurant_Id")
    private Restaurant restaurant;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "type")
    private String type;

    public Food() {
    }

    /**
     * Constructor of menu entity.
     *
     * @param restaurant  - restaurant that provides the food
     * @param description - description of the food
     * @param price       - price of food
     * @param type        -type of food
     * @param name        - name of thefood
     */
    public Food(Restaurant restaurant, String description, double price, String type, String name) {
        this.restaurant = restaurant;
        this.description = description;
        this.price = price;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Food)) {
            return false;
        }
        Food food = (Food) o;
        return id == food.id
            && price == food.price
            && restaurant.equals(food.restaurant)
            && description.equals(food.description)
            && name.equals(food.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurant, description, price, name);
    }
}
