package ktpm17ctt.g6.orderservice.dto.feinClient.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorResponse {
    String id;
    String name;
    String code;
}
