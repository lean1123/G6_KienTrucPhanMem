package ktpm17ctt.g6.orderservice.services;

import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderDetailResponse;
import ktpm17ctt.g6.orderservice.entities.Order;

public interface OrderDetailService {
    OrderDetailResponse save(OrderDetailRequest request, Order order) throws Exception;
}
