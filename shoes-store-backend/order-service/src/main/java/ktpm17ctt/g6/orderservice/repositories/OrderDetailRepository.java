package ktpm17ctt.g6.orderservice.repositories;

import ktpm17ctt.g6.orderservice.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findOrderDetailByOrder_Id(String orderId);
}
