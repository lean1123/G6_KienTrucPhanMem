package ktpm17ctt.g6.product.dto.request;

import ktpm17ctt.g6.product.entity.Category;
import ktpm17ctt.g6.product.entity.ProductCollection;
import ktpm17ctt.g6.product.entity.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String description;
    double rating;
    Instant createdDate;
    Instant modifiedDate;
    Gender gender;
    String collectionId;
    String categoryId;
}
