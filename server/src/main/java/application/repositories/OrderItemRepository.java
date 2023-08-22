package application.repositories;

import application.entities.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> findOrderItemsByFood_IdAndOrder_Id(Integer foodId, Integer orderId);
}
