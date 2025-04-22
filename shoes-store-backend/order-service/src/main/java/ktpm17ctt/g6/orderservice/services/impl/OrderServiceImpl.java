package ktpm17ctt.g6.orderservice.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.orderservice.dto.common.ApiResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.identity.AccountResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.payment.PaymentResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.payment.RefundResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.user.UserResponse;
import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.entities.PaymentMethod;
import ktpm17ctt.g6.orderservice.mapper.OrderMapper;
import ktpm17ctt.g6.orderservice.repositories.OrderRepository;
import ktpm17ctt.g6.orderservice.repositories.httpClients.IdentityClient;
import ktpm17ctt.g6.orderservice.repositories.httpClients.PaymentClient;
import ktpm17ctt.g6.orderservice.repositories.httpClients.ProductItemClient;
import ktpm17ctt.g6.orderservice.repositories.httpClients.UserClient;
import ktpm17ctt.g6.orderservice.services.OrderDetailService;
import ktpm17ctt.g6.orderservice.services.OrderService;
import ktpm17ctt.g6.orderservice.util.GetIpAddress;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailService orderDetailService;
    PaymentClient paymentClient;
    UserClient userClient;
    ProductItemClient productItemClient;
    IdentityClient identityClient;
//    KafkaTemplate<String, Object> kafkaTemplate;
//    KafkaService kafkaService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse save(OrderCreationRequest request, HttpServletRequest req) throws Exception {
//        Check user dat hang
        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()){
            email = authentication.getName();
            log.info("Email Logged: {}", email);
        }

        String userId = this.getUserIdFromEmail(email);

        if(userId == null){
            throw new Exception("User not exist in system");
        }


        List<OrderDetailRequest> orderDetails = request.getOrderDetails();
        double total = this.getTotalPrice(orderDetails);

        Order entity = Order.builder()
                .total(total)
                .userId(userId)
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .status(OrderStatus.PENDING)
                .createdDate(Instant.now())
                .total(total)
                .build();

        entity = orderRepository.save(entity);

        for (OrderDetailRequest orderDetail : orderDetails) {
            orderDetailService.save(orderDetail, entity);
        }

        String paymentUrl = "";
        if (entity.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
            try {
                paymentUrl = paymentClient
                        .createNewPayment(entity.getId(), String.valueOf(Long.valueOf((long) total)), GetIpAddress.getIpAddress(req))
                        .getBody().getPaymentUrl();
            }catch (Exception e){
                log.error("Error while creating payment" + entity.getId(), e);
                entity.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(entity);
            }
        }

        if(paymentUrl == null || paymentUrl.isEmpty()) {
            throw new Exception("Payment URL is empty in payment service");
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

    private String getUserIdFromEmail(String email) throws  Exception{
        ApiResponse<AccountResponse> accountResponse = identityClient.getAccountByEmail(email);

        if(accountResponse.getResult() == null){
            throw new NullPointerException("Account not found");
        }

        String accountId = accountResponse.getResult().getId();

        ApiResponse<UserResponse> userResponse = userClient.getUserByAccountId(accountId);
        if(userResponse.getResult() == null){
            throw new NullPointerException("User not found");
        }
        return userResponse.getResult().getId();
    }

    private double getTotalPrice(List<OrderDetailRequest> orderDetails) throws Exception {

        double total = 0;

       for (OrderDetailRequest orderDetail : orderDetails) {
            ProductItemResponse productItemResponse = productItemClient.getProductItem(orderDetail.getProductItemId()).getResult();

            if (productItemResponse == null) {
                throw new Exception("Product item not found");
            }

            total += orderDetail.getQuantity() * productItemResponse.getPrice();
        }

        return total;
    }
}

