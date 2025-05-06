package ktpm17ctt.g6.orderservice.dto.feinClient.product;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItemRequest {
    @Nullable
    String id;
    double price;
    List<String> images;
    String colorId;
    String quantityOfSize;
    String productId;
    Status status;
}
