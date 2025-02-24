package ktpm17ctt.g6.commondto.dtos.requests;


import ktpm17ctt.g6.commondto.enums.Gender;
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
