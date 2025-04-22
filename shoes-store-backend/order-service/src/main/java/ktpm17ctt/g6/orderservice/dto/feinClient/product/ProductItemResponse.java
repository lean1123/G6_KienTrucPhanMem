package ktpm17ctt.g6.orderservice.dto.feinClient.product;



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
    List<ktpm17ctt.g6.orderservice.dto.feinClient.product.QuantityOfSize> quantityOfSize;
    ProductResponse product;
    Status status;
}
