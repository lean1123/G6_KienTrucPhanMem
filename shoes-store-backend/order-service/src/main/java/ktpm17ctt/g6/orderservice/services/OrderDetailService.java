package ktpm17ctt.g6.orderservice.services;

import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderDetailResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetailResponse save(OrderDetailRequest request, Order order) throws Exception;

    List<OrderDetailResponse> findOrderDetailByOrder_Id(String orderId);
}
