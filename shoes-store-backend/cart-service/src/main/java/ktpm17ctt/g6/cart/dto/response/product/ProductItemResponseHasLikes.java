package ktpm17ctt.g6.cart.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItemResponseHasLikes {
    String id;
    double price;
    List<String> images;
    ColorResponse color;
    List<QuantityOfSize> quantityOfSize;
    ProductResponse product;
    Status status;
    List<String> likes;
    @JsonProperty("isActive")
    boolean isActive;
}
