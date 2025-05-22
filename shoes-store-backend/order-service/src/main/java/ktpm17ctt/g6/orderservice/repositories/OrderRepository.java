package ktpm17ctt.g6.orderservice.repositories;

import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findOrdersByUserId(String userId);
}
