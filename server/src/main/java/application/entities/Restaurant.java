package application.entities;

import java.sql.Time;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "opening_time")
    private Time openingTime;

    @NotNull
    @Column(name = "closing_time")
    private Time closingTime;

    @NotNull
    @ManyToOne
    @Cascade(CascadeType.MERGE)
    @JoinColumn(name = "building_id")
    private Building building;

    public Restaurant() {

    }

    /**
     * Constructor for Restaurant entity.
     *  @param name        - name of restaurant
     * @param openingTime - opening time of restaurant
     * @param closingTime - closing time of restaurant
     * @param building Location of the restaurant.
     */
    public Restaurant(String name, Time openingTime, Time closingTime, Building building) {
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.building = building;
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

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        Restaurant that = (Restaurant) o;
        return id == that.id
            && name.equals(that.name)
            && openingTime.equals(that.openingTime)
            && closingTime.equals(that.closingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, openingTime, closingTime, building);
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
