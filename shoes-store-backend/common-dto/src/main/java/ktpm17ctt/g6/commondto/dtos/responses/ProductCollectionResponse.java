package ktpm17ctt.g6.commondto.dtos.responses;


import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCollectionResponse {
    String id;
    String name;
    BrandResponse brand;
}
