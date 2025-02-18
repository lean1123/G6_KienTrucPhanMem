package java.ktpm17ctt.g6.commondto.dtos.responses;

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
    Instant createdDate;
    Instant modifiedDate;
    java.ktpm17ctt.g6.commondto.enums.Gender gender;
    ProductCollectionResponse collection;
    CategoryResponse category;
}
