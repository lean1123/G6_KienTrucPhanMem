package ktpm17ctt.g6.cart.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String code;
    String description;
    double rating;
    Instant createdDate;
    Instant modifiedDate;
    Gender gender;
    ProductCollectionResponse collection;
    CategoryResponse category;
    String type;
}
