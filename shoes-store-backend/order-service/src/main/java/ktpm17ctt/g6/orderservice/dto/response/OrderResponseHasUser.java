package ktpm17ctt.g6.orderservice.dto.response;

import ktpm17ctt.g6.orderservice.dto.feinClient.user.AddressResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.user.UserResponse;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.entities.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderResponseHasUser {
    String id;
    double total;
    Instant createdDate;
    OrderStatus status;
    UserResponse user;
    PaymentMethod paymentMethod;
    List<OrderDetailResponse> orderDetails;
    String paymentUrl;
    AddressResponse address;
    boolean isPayed;
}
