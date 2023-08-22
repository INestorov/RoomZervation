package application.entities;

import java.sql.Timestamp;
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
import org.springframework.lang.Nullable;

@Entity
@Table(name = "bikeRental")
public class BikeRental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @NotNull
    @ManyToOne
    private Bike bike;

    @NotNull
    @Column(name = "start")
    private Timestamp start;

    @Nullable
    @Column(name = "end")
    private Timestamp end;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public BikeRental() {
    }

    /**
     * Create a bikeRental instance when a user returns the bike.
     *
     * @param bike  bike
     * @param user  user
     * @param start start
     */

    public BikeRental(Bike bike, User user, Timestamp start, Timestamp end) {
        this.bike = bike;
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BikeRental)) {
            return false;
        }
        BikeRental that = (BikeRental) o;
        return id == that.id
            && Objects.equals(bike, that.bike)
            && Objects.equals(start, that.start)
            && Objects.equals(end, that.end)
            && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
            bike,
            start, end, user);
    }
}
