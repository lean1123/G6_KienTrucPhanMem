package java.ktpm17ctt.g6.commondto.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.ktpm17ctt.g6.commondto.dtos.QuantityOfSize;
import java.ktpm17ctt.g6.commondto.enums.Status;
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
