package application.repositories;

import application.entities.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findOrdersByReservation_User_Username(String username);
}
