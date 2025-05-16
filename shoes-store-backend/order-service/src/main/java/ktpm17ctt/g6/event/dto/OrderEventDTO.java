package ktpm17ctt.g6.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEventDTO {
    private String userId;
    private String userEmail;

}
