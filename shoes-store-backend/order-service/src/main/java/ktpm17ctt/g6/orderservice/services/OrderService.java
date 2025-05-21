package ktpm17ctt.g6.orderservice.services;

import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.orderservice.dto.request.OrderCreationRequest;
import ktpm17ctt.g6.orderservice.dto.response.OrderResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {
    OrderResponse save(OrderCreationRequest request,
                       HttpServletRequest httpServletRequest) throws Exception;

    OrderResponse findById(String s) throws Exception;

    OrderResponse handleUpdateOrderForPaymentFailed(String orderId) throws Exception;

    @Transactional
    OrderResponse cancelingOrder(String orderId, HttpServletRequest request) throws Exception;

    void deleteById(String s);

    List<OrderResponse> getMyOrders() throws Exception;

    @Transactional(rollbackFor = Exception.class)
    OrderResponse updatePaymentStatusForOrder(String orderId, boolean isPayed) throws Exception;

    List<OrderResponse> getAllOrders() throws Exception;
}
