package ktpm17ctt.g6.orderservice.dto.response;

import jakarta.persistence.*;
import ktpm17ctt.g6.orderservice.entities.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    String id;
    int quantity;
    double price;
    String productItemId;
    String orderId;
}
