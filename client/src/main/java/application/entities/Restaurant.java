package application.entities;

import java.sql.Time;
import java.util.Date;
import java.util.Objects;

public class Restaurant {

    private int id;
    private String name;
    private Time openingTime;
    private Time closingTime;
    private String location;
    private Building building;

    /**
     * Create a restaurant instance for posting to the server.
     *
     * @param name        Name of the restaurant
     * @param openingTime time the restaurant opens
     * @param closingTime time it closes
     * @param building    building where the restaurant is located
     */
    public Restaurant(String name, Time openingTime, Time closingTime, Building building) {
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.building = building;
    }

    /**
     * Create a restaurant instance to show in the table.
     *
     * @param name        Name of the restaurant
     * @param openingTime time the restaurant opens
     * @param closingTime time it closes
     * @param location    name of the building where the restaurant is located
     */
    public Restaurant(int id, String name, Time openingTime, Time closingTime, String location) {
        this.id = id;
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Restaurant that = (Restaurant) o;
        return id == that.id
            && name.equals(that.name)
            && openingTime.equals(that.openingTime)
            && closingTime.equals(that.closingTime)
            && location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, openingTime, closingTime, location);
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String building) {
        this.location = building;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
