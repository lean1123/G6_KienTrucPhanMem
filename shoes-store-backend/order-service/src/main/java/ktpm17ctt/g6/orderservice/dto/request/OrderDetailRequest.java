package ktpm17ctt.g6.orderservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    String productItemId;
    int quantity;
    double price;
    String orderId;
}
