package ktpm17ctt.g6.orderservice.dto.request;

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
public class OrderCreationRequest {
    double total;
    String userId;
    PaymentMethod paymentMethod;
    String userEmail;  // Thêm trường userEmail
    List<OrderDetailRequest> orderDetails;
}
