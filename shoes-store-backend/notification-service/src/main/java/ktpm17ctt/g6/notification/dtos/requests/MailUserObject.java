package ktpm17ctt.g6.notification.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailUserObject {
    String name;
    String email;
}
