package ktpm17ctt.g6.product.dto.response;

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
public class ProductItemResponse {
    String id;
    double price;
    List<String> images;
    ColorResponse color;
    List<QuantityOfSize> quantityOfSize;
    ProductResponse product;
    Status status;
}
