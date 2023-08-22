package application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "bike")
public class Bike {

    @JsonIgnore
    @Nullable
    @Column(name = "bike_rental")
    @OneToMany(mappedBy = "bike", cascade = CascadeType.ALL)
    Set<BikeRental> bikeRentals = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Nullable
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Nullable
    @Column(name = "reserved_until")
    private Timestamp reservedUntil;

    public Bike() {
    }

    /**
     * Create a bike instance.
     * When a bike is available, building is not null. When a bike is rented, building is null.
     *
     * @param building building
     */
    public Bike(Building building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bike bike = (Bike) o;
        return id == bike.id
            &&
            Objects.equals(building, bike.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, building);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Set<BikeRental> getBikeRentals() {
        return bikeRentals;
    }

    public void setBikeRentals(Set<BikeRental> bikeRentals) {
        this.bikeRentals = bikeRentals;
    }

    public void addBikeRental(BikeRental bikeRental) {
        bikeRentals.add(bikeRental);
    }

    @Nullable
    public User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }

    public Timestamp getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(Timestamp reservedUntil) {
        this.reservedUntil = reservedUntil;
    }
}
