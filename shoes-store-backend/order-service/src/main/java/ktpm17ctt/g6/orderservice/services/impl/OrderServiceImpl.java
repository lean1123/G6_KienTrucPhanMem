package ktpm17ctt.g6.orderservice.services.impl;

import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.mapper.OrderMapper;
import ktpm17ctt.g6.orderservice.repositories.OrderRepository;
import ktpm17ctt.g6.orderservice.services.OrderDetailService;
import ktpm17ctt.g6.orderservice.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    OrderDetailService orderDetailService;


    @Override
    @Transactional
    public OrderResponse save(OrderCreationRequest request) throws Exception {

//        Tim user dat hang

        Order entity = Order.builder()
                .total(request.getTotal())
//                Lay id cua user tra ve
                .userId(request.getUserId())
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .createdDate(Instant.now())
                .build();

        entity = orderRepository.save(entity);

        List<OrderDetailRequest> orderDetails = request.getOrderDetails();

        try {
            for (OrderDetailRequest orderDetail : orderDetails) {
                orderDetailService.save(orderDetail, entity);
            }
        } catch (Exception e) {
            log.error("Error while saving order details", e);
            throw new Exception("Error while saving order details");
        }

        return orderMapper.orderToOrderResponse(entity);
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

