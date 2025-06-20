package ktpm17ctt.g6.orderservice.dto.response;

import ktpm17ctt.g6.orderservice.dto.feinClient.user.AddressResponse;
import ktpm17ctt.g6.orderservice.entities.OrderStatus;
import ktpm17ctt.g6.orderservice.entities.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    double total;
    Instant createdDate;
    OrderStatus status;
    String userId;
    PaymentMethod paymentMethod;
    List<OrderDetailResponse> orderDetails;
    String paymentUrl;
    AddressResponse address;
    boolean isPayed;
}
