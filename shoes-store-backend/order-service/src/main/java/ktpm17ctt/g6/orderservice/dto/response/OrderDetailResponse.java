package ktpm17ctt.g6.orderservice.dto.response;

import jakarta.persistence.*;
import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.product.ProductItemResponseHasLikes;
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
    ProductItemResponseHasLikes productItem;
    String orderId;
    int size;
}
