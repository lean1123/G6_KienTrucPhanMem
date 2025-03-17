package ktpm17ctt.g6.orderservice.dto.request;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class OrderRequest {
    String id;
    double total;
    Instant createdDate;
    OrderStatus status;
    String userId;
    String feedbackId;
    PaymentMethod paymentMethod;
}
