package java.ktpm17ctt.g6.commondto.dtos.responses;

import lombok.experimental.FieldDefaults;
import lombok.*;

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
