package ktpm17ctt.g6.commondto.dtos.responses;

import ktpm17ctt.g6.commondto.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import ktpm17ctt.g6.commondto.dtos.QuantityOfSize;

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
