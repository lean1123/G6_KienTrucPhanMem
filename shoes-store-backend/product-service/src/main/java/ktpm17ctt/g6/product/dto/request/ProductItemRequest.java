package ktpm17ctt.g6.product.dto.request;

import ktpm17ctt.g6.product.entity.Color;
import ktpm17ctt.g6.product.entity.QuantityOfSize;
import ktpm17ctt.g6.product.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItemRequest {
    double price;
    List<String> images;
    String colorId;
    List<QuantityOfSize> quantityOfSize;
    String productId;
    Status status;
}
