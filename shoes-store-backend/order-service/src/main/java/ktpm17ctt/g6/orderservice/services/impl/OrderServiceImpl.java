package ktpm17ctt.g6.orderservice.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.event.dto.PaymentUrlCreationReq;
import ktpm17ctt.g6.event.dto.PaymentUrlResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.payment.PaymentResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.payment.RefundResponse;
import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.mapper.OrderMapper;
import ktpm17ctt.g6.orderservice.repositories.OrderRepository;
import ktpm17ctt.g6.orderservice.repositories.httpClients.PaymentClient;
import ktpm17ctt.g6.orderservice.services.OrderDetailService;
import ktpm17ctt.g6.orderservice.services.OrderService;
import ktpm17ctt.g6.orderservice.util.GetIpAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    KafkaTemplate<String, Object> kafkaTemplate;
    KafkaService kafkaService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse save(OrderCreationRequest request, HttpServletRequest req) throws Exception {


        List<OrderDetailRequest> orderDetails = request.getOrderDetails();
        double total = orderDetails.stream().mapToDouble(OrderDetailRequest::getPrice).sum();

//        Check user dat hang
        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){
            email = authentication.getName();
            log.info("Email Logged: {}", email);
        }

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

        for (OrderDetailRequest orderDetail : orderDetails) {
            orderDetailService.save(orderDetail, entity);
        }

//        String paymentUrl = "";
//        if (entity.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
//            try {
//                paymentUrl = paymentClient
//                        .createNewPayment(entity.getId(), String.valueOf(Long.valueOf((long) 1000000)), GetIpAddress.getIpAddress(req))
//                        .getBody().getPaymentUrl();
//            }catch (Exception e){
//                log.error("Error while creating payment" + entity.getId(), e);
////                Cap nhat lai trang thai don hang
//                entity.setStatus(OrderStatus.PAYMENT_FAILED);
//                orderRepository.save(entity);
//            }
//        }

        PaymentUrlCreationReq paymentUrlCreationReq = PaymentUrlCreationReq.builder()
                .orderId(entity.getId())
                .amount(String.valueOf((long) entity.getTotal()))
                .ipAddress(GetIpAddress.getIpAddress(req))
                .build();

        kafkaTemplate.send("payment-request", paymentUrlCreationReq)
                .whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send Kafka message", ex);
                throw new RuntimeException("Payment service unavailable. Please try again.");
            } else {
                log.info("Kafka message sent successfully: {}", result.getProducerRecord().value());
            }
        });

        PaymentUrlResponse paymentUrlResponse = kafkaService.getPaymentUrlResponse();
        log.info("Payment URL response: {}", paymentUrlResponse);


        return OrderResponse.builder()
                .id(entity.getId())
                .createdDate(entity.getCreatedDate())
                .userId(entity.getUserId())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .orderDetails(orderDetailService.findOrderDetailByOrder_Id(entity.getId()))
                .total(entity.getTotal())
                .paymentUrl(paymentUrlResponse.getPaymentUrl())
                .build();
    }

    @Override
    public OrderResponse findById(String s) throws Exception {
        return orderRepository.findById(s)
                .map(orderMapper::orderToOrderResponse)
                .orElseThrow(() -> new Exception("Order not found"));
    }

    @Transactional
    @Override
    public OrderResponse canclingOrder(String orderId, HttpServletRequest request) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (order.getStatus().equals(OrderStatus.PENDING)) {
            order.setStatus(OrderStatus.CANCELLED);

            PaymentResponse paymentResponse = paymentClient.getPaymentByOrderId(orderId).getBody();

            log.info("Payment response: {}", paymentResponse);
            assert paymentResponse != null;
            log.info("Payment status: {}", paymentResponse.getStatus());

            if (paymentResponse != null && paymentResponse.getStatus().toUpperCase().equalsIgnoreCase("SUCCESS")) {
                log.info("Refunding payment");
                try {
                    RefundResponse refundResponse = paymentClient.refundPayment(
                            order.getId(),
                            "02",
                            String.valueOf((long) paymentResponse.getAmount() / 100),
                            "merchant",
                            paymentResponse.getTransactionDate(),
                            GetIpAddress.getIpAddress(request),
                            paymentResponse.getTransactionId()
                    ).getBody();
                    log.info("Refund response: {}", refundResponse);
                } catch (Exception e) {
                    log.error("Error while refunding payment", e);
                    throw new Exception("Error while refunding payment");
                }
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return orderMapper.orderToOrderResponse(order);
    }

    public void deleteById(String s) {
        orderRepository.deleteById(s);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}

