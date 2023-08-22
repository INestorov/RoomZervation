package application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

@Entity
@Table(name = "user")
public class  User {
    @Id
    private int id;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "mail")
    private String mail;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<BikeRental> bikeRentals = new HashSet<>();

    public User() {
    }

    /**
     * Create a new User instance.
     *
     * @param id       id
     * @param username username
     * @param password password
     * @param type     employee type
     */
    public User(int id, String username, String password, UserType type, String phoneNumber,
                String mail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Set<BikeRental> getBikeRentals() {
        return bikeRentals;
    }

    public void setBikeRentals(Set<BikeRental> bikeRentals) {
        this.bikeRentals = bikeRentals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id
            && type.equals(user.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

}
