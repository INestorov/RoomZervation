package application.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    @JoinColumn(name = "reservation_Id")
    private Reservation reservation;

    @JsonManagedReference
    @OneToMany(cascade = javax.persistence.CascadeType.PERSIST,
        fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderItem> foods = new HashSet<>();

    @NotNull
    @Column(name = "date")
    private Timestamp date;

    public Order() {
    }

    /**
     * Constructor of order entity.
     *
     * @param reservation - order was made through a reservation
     * @param foods       - menus ordered by the user
     * @param date        - date of purchase
     */
    public Order(Reservation reservation, Set<OrderItem> foods, Timestamp date) {
        this.reservation = reservation;
        this.foods = foods;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Set<OrderItem> getFoods() {
        return foods;
    }

    public void setFoods(Set<OrderItem> foods) {
        this.foods = foods;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return id == order.id
            && reservation.equals(order.reservation)
            && foods.equals(order.foods)
            && date.equals(order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservation, date, foods);
    }
}


