package ktpm17ctt.g6.orderservice.services.impl;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.orderservice.dto.common.ApiResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.identity.AccountResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.payment.PaymentResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.payment.RefundResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.user.AddressResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.user.UserResponse;
import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderDetailRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import ktpm17ctt.g6.orderservice.entities.Order;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.entities.PaymentMethod;
import ktpm17ctt.g6.orderservice.kafka.OrderEventProducer;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailService orderDetailService;
    private final PaymentClient paymentClient;
    private final UserClient userClient;
    private final ProductItemClient productItemClient;
    private final IdentityClient identityClient;
    private final OrderEventProducer orderEventProducer;
//    KafkaTemplate<String, Object> kafkaTemplate;
//    KafkaService kafkaService;

    @Value("${gateway.health.check.url}")
    private String GATEWAY_HEALTH_CHECK_URL;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse save(OrderCreationRequest request, HttpServletRequest req) throws Exception {
        log.info("Gateway health check URL: {}", GATEWAY_HEALTH_CHECK_URL);

        log.info("Gateway is healthy: {}", this.isGatewayHealthy(GATEWAY_HEALTH_CHECK_URL));

//        check gateway health
        if (!isGatewayHealthy(GATEWAY_HEALTH_CHECK_URL)) {
            throw new Exception("Gateway is not ready!");
        }

//        Check user dat hang
        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
            log.info("Email Logged: {}", email);
        }

        String userId = this.getUserIdFromEmail(email);

        if (userId == null) {
            throw new Exception("User not exist in system");
        }

        AddressResponse addressResponse = userClient.getAddressById(request.getAddressId()).getResult();


        List<OrderDetailRequest> orderDetails = request.getOrderDetails();
        double total = this.getTotalPrice(orderDetails);

        log.info("Total price: {}", total);

        Order entity = entity = Order.builder()
                .total(total)
                .userId(userId)
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .status(OrderStatus.PENDING)
                .createdDate(Instant.now())
                .total(total)
                .addressId(request.getAddressId())
                .isPayed(false)
                .build();


        entity = orderRepository.save(entity);

        log.info("Order created: {}", entity);

        for (OrderDetailRequest orderDetail : orderDetails) {
            orderDetailService.save(orderDetail, entity);
        }


        if (entity.getPaymentMethod().toString().equalsIgnoreCase(PaymentMethod.CASH.toString())) {


            return OrderResponse.builder()
                    .id(entity.getId())
                    .createdDate(entity.getCreatedDate())
                    .userId(entity.getUserId())
                    .paymentMethod(entity.getPaymentMethod())
                    .status(entity.getStatus())
                    .orderDetails(orderDetailService.findOrderDetailByOrder_Id(entity.getId()))
                    .total(entity.getTotal())
                    .address(addressResponse)
                    .build();
        }

        String paymentUrl = "";
        if (entity.getPaymentMethod().equals(PaymentMethod.VNPAY)) {
            try {
                paymentUrl = paymentClient
                        .createNewPayment(entity.getId(), String.valueOf(Long.valueOf((long) total)), GetIpAddress.getIpAddress(req))
                        .getBody().getPaymentUrl();
            } catch (Exception e) {
                log.error("Error while creating payment" + entity.getId(), e);
                //Xoa don hang
                orderRepository.deleteById(entity.getId());
            }
        }

        if (paymentUrl == null || paymentUrl.isEmpty()) {
            throw new Exception("Payment URL is empty in payment service");
        }

        log.info("Payment URL: {}", paymentUrl);

        orderEventProducer.sendOrderSuccessEvent(email, userId); // Truyền cả email và userId
        log.info("Sent order success event for order {} to user {}", entity.getId(), email);


        return OrderResponse.builder()
                .id(entity.getId())
                .createdDate(entity.getCreatedDate())
                .userId(entity.getUserId())
                .paymentMethod(entity.getPaymentMethod())
                .status(entity.getStatus())
                .orderDetails(orderDetailService.findOrderDetailByOrder_Id(entity.getId()))
                .total(entity.getTotal())
                .paymentUrl(paymentUrl)
                .address(addressResponse)
                .build();
    }

    @Override
    public OrderResponse findById(String s) throws Exception {
        return orderRepository.findById(s)
                .map(order -> {
                    AddressResponse addressResponse = null;
                    try {
                        addressResponse = userClient.getAddressById(order.getAddressId()).getResult();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return OrderResponse.builder()
                            .id(order.getId())
                            .createdDate(order.getCreatedDate())
                            .userId(order.getUserId())
                            .paymentMethod(order.getPaymentMethod())
                            .status(order.getStatus())
                            .orderDetails(orderDetailService.findOrderDetailByOrder_Id(order.getId()))
                            .total(order.getTotal())
                            .address(addressResponse)
                            .build();
                })
                .orElseThrow(() -> new Exception("Order not found"));
    }

    @Override
    public OrderResponse handleUpdateOrderForPaymentFailed(String orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (order.getStatus().equals(OrderStatus.PENDING)) {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
            order = orderRepository.save(order);
        } else {
            throw new Exception("Order cannot be updated for pay failed");
        }

        AddressResponse addressResponse = userClient.getAddressById(order.getAddressId()).getResult();

        return OrderResponse.builder()
                .id(order.getId())
                .createdDate(order.getCreatedDate())
                .userId(order.getUserId())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .orderDetails(orderDetailService.findOrderDetailByOrder_Id(order.getId()))
                .total(order.getTotal())
                .address(addressResponse)
                .isPayed(order.isPayed())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse cancelingOrder(String orderId, HttpServletRequest request) throws Exception {

//        check gateway health
        if (!isGatewayHealthy(GATEWAY_HEALTH_CHECK_URL)) {
            throw new Exception("Gateway is not ready!");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if(!order.getStatus().toString().equalsIgnoreCase(OrderStatus.PENDING.toString())){
            throw  new Exception("Order cannot be cancelled");
        }

        if (order.getPaymentMethod().toString().equalsIgnoreCase("VNPAY")) {
            order.setStatus(OrderStatus.CANCELLED);

            PaymentResponse paymentResponse = null;
            try {
                ResponseEntity<PaymentResponse> responseEntity = paymentClient.getPaymentByOrderId(orderId);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    paymentResponse = responseEntity.getBody();
                }
            } catch (FeignException.Unauthorized | FeignException.NotFound ex) {
                log.warn("Payment not found or unauthorized for order {}", orderId);
                // Nếu cần, bạn có thể throw 1 exception custom ở đây nếu lỗi cần fail luôn.
                throw new Exception("Error while getting payment information for VNPay order");
            } catch (Exception ex) {
                log.error("Unexpected error while calling payment service", ex);
                throw new Exception("Error while getting payment information");
            }

            if (paymentResponse != null && "SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
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
        AddressResponse addressResponse = userClient.getAddressById(order.getAddressId()).getResult();

        return OrderResponse.builder()
                .id(order.getId())
                .createdDate(order.getCreatedDate())
                .userId(order.getUserId())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .orderDetails(orderDetailService.findOrderDetailByOrder_Id(order.getId()))
                .total(order.getTotal())
                .address(addressResponse)
                .isPayed(order.isPayed())
                .build();
    }


    @Override
    public void deleteById(String s) {
        orderRepository.deleteById(s);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    private String getUserIdFromEmail(String email) throws Exception {
        log.info("Email in get user id from email: {}", email);
        ApiResponse<AccountResponse> accountResponse = identityClient.getAccountByEmail(email);

        if (accountResponse.getResult() == null) {
            throw new NullPointerException("Account not found");
        }

        String accountId = accountResponse.getResult().getId();

        ApiResponse<UserResponse> userResponse = userClient.getUserByAccountId(accountId);
        if (userResponse.getResult() == null) {
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

    @Override
    public List<OrderResponse> getMyOrders() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Email Logged: {}", email);
        String userId = this.getUserIdFromEmail(email);

        List<Order> orders = orderRepository.findOrdersByUserId(userId);

        return orders.stream()
                .map(order -> {
                    AddressResponse addressResponse = null;
                    try {
                        addressResponse = userClient.getAddressById(order.getAddressId()).getResult();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return OrderResponse.builder()
                            .id(order.getId())
                            .createdDate(order.getCreatedDate())
                            .userId(order.getUserId())
                            .paymentMethod(order.getPaymentMethod())
                            .status(order.getStatus())
                            .orderDetails(orderDetailService.findOrderDetailByOrder_Id(order.getId()))
                            .total(order.getTotal())
                            .address(addressResponse)
                            .isPayed(order.isPayed())
                            .build();
                })
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse updatePaymentStatusForOrder(String orderId, boolean isPayed) throws Exception {

        log.info("Updating payment status for orderId: {}", orderId);
        log.info("Payment status updated to: {}", isPayed);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setPayed(isPayed);
        order = orderRepository.save(order);

        AddressResponse addressResponse = userClient.getAddressById(order.getAddressId()).getResult();

        return OrderResponse.builder()
                .id(order.getId())
                .createdDate(order.getCreatedDate())
                .userId(order.getUserId())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .orderDetails(orderDetailService.findOrderDetailByOrder_Id(order.getId()))
                .total(order.getTotal())
                .address(addressResponse)
                .build();

    }

    private boolean isGatewayHealthy(String gatewayHealthUrl) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(gatewayHealthUrl, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false; // Nếu Gateway chết, timeout, lỗi 500,... thì coi như unhealthy
        }
    }
}

