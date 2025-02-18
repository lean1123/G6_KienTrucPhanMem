package java.ktpm17ctt.g6.commondto.dtos.requests;


import lombok.experimental.FieldDefaults;
import lombok.*;

import java.ktpm17ctt.g6.commondto.dtos.QuantityOfSize;
import java.ktpm17ctt.g6.commondto.enums.Status;
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
