package ktpm17ctt.g6.product.dto.response;

import ktpm17ctt.g6.product.entity.enums.Type;
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
    String description;
    double rating;
    Type type;
    CategoryResponse category;
    Instant createdDate;
    Instant modifiedDate;
}
