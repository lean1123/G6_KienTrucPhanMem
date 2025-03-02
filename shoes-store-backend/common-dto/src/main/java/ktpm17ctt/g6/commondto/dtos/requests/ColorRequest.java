package ktpm17ctt.g6.commondto.dtos.requests;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorRequest {
    String name;
    String code;
}
