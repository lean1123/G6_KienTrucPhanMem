package ktpm17ctt.g6.paymentservice.dtos.feinClient;


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
}
