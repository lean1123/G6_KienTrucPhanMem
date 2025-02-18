package ktpm17ctt.g6.orderservice.repositories;

import ktpm17ctt.g6.orderservice.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
