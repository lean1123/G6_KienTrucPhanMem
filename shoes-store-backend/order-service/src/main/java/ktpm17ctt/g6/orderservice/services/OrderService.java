package ktpm17ctt.g6.orderservice.services;

import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse save(OrderCreationRequest request) throws Exception;
}
