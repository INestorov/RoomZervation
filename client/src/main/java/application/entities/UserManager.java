package application.entities;

public class UserManager {

    private static final UserManager instance = new UserManager();
    private User user;
    private int reservationId;
    private Basket basket;

    /**
     * private constructor to prevent other classes creating another instance.
     */
    private UserManager() {
    }

    public static UserManager getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}
