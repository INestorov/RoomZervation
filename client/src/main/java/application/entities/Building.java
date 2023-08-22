package application.entities;

import java.sql.Time;

public class Building {

    private String name;
    private int nbikes;
    private int id;
    private Time openingTime;
    private Time closingTime;

    /**
     * Create a building instance.
     * The number of bikes associated with the building is set to 0
     *
     * @param name Name of the building
     */
    public Building(String name) {
        this.name = name;
        this.nbikes = 0;
    }

    /**
     * Create a building instance.
     * @param name   Name of the building
     * @param nbikes Number of bikes located at the building
     * @param id Id of the building.
     * @param openingTime Time the building opens
     * @param closingTime Time the building closes
     */
    public Building(String name, int nbikes, int id, Time openingTime, Time closingTime) {
        this.name = name;
        this.nbikes = nbikes;
        this.id = id;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Building) {
            Building that = (Building) other;
            return this.name.equals(that.name)
                && this.nbikes == that.nbikes;
        } else {
            return false;
        }
    }

    public int getNbikes() {
        return nbikes;
    }

    public void setNbikes(int bikes) {
        this.nbikes = bikes;
    }

    public void addBike() {
        nbikes++;
    }

    public void removeBike() {
        nbikes--;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }
}
