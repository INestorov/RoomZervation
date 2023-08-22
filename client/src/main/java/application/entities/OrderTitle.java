package application.entities;

import java.util.Date;

public class OrderTitle {

    private double price;
    private String name;
    private Date time;
    private int id;

    /**
     * Create an OrderTitle instance.
     * @param price The total price of the order.
     * @param name Name of the restaurant.
     * @param time Time the order was issued.
     * @param id Id of the order.
     */
    public OrderTitle(double price, String name, Date time, int id) {
        this.price = price;
        this.name = name;
        this.time = time;
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
