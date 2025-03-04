package ktpm17ctt.g6.commondto.dtos.responses;

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
