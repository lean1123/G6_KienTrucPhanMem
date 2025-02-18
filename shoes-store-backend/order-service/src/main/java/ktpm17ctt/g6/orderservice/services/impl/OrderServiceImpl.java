package ktpm17ctt.g6.orderservice.services.impl;

import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.mapper.OrderMapper;
import ktpm17ctt.g6.orderservice.repositories.OrderRepository;
import ktpm17ctt.g6.orderservice.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    @Override
    public OrderResponse save(OrderCreationRequest request) {

        Order entity = Order.builder()
                .total(request.getTotal())
                .userId(request.getUserId())
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .createdDate(Instant.now())
                .paymentMethod(request.getPaymentMethod())
                .build();

        List<OrderDetailRequest> orderDetails = request.getOrderDetails();

        return orderMapper.orderToOrderResponse(orderRepository.save(entity));
    }

    public Optional<Order> findById(String s) {
        return orderRepository.findById(s);
    }

    public void deleteById(String s) {
        orderRepository.deleteById(s);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}

