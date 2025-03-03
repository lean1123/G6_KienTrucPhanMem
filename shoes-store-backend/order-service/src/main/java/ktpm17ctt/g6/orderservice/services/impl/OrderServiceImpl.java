package ktpm17ctt.g6.orderservice.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.commondto.utils.GetIpAddress;
import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.entities.PaymentMethod;
import ktpm17ctt.g6.orderservice.mapper.OrderMapper;
import ktpm17ctt.g6.orderservice.repositories.OrderRepository;
import ktpm17ctt.g6.orderservice.repositories.httpClients.PaymentClient;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailService orderDetailService;
    PaymentClient paymentClient;


    @Override
    @Transactional
    public OrderResponse save(OrderCreationRequest request, HttpServletRequest req) throws Exception {

        List<OrderDetailRequest> orderDetails = request.getOrderDetails();
        double total = orderDetails.stream().mapToDouble(OrderDetailRequest::getPrice).sum();

//        Check user dat hang


        Order entity = Order.builder()
                .total(request.getTotal())
//                Lay id cua user tra ve
                .userId(request.getUserId())
                .paymentMethod(request.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .createdDate(Instant.now())
                .total(total)
                .build();

        entity = orderRepository.save(entity);


        try {
            for (OrderDetailRequest orderDetail : orderDetails) {
                orderDetailService.save(orderDetail, entity);
            }
        } catch (Exception e) {
            log.error("Error while saving order details", e);
            throw new Exception("Error while saving order details");
        }

        String paymentUrl = "";
        if (entity.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
            paymentUrl = paymentClient
                    .createNewPayment(entity.getId(), String.valueOf(Long.valueOf((long) 1000000)), GetIpAddress.getIpAddress(req))
                    .getBody().getPaymentUrl();
        }



        return OrderResponse.builder()
                .id(entity.getId())
                .createdDate(entity.getCreatedDate())
                .userId(entity.getUserId())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .orderDetails(orderDetailService.findOrderDetailByOrder_Id(entity.getId()))
                .total(entity.getTotal())
                .paymentUrl(paymentUrl)
                .build();
    }

    @Override
    public OrderResponse findById(String s) throws Exception {
        return orderRepository.findById(s)
                .map(orderMapper::orderToOrderResponse)
                .orElseThrow(() -> new Exception("Order not found"));
    }

    public void deleteById(String s) {
        orderRepository.deleteById(s);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}

