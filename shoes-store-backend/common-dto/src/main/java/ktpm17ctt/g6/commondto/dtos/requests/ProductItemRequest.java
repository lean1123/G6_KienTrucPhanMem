package ktpm17ctt.g6.commondto.dtos.requests;


import ktpm17ctt.g6.commondto.enums.Status;
import lombok.experimental.FieldDefaults;
import lombok.*;

import ktpm17ctt.g6.commondto.dtos.QuantityOfSize;

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
