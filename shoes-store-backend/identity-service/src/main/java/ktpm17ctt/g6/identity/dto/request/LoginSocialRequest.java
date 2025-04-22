package ktpm17ctt.g6.identity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginSocialRequest {
    String email;
    String name;
    String avatar;
    String googleAccountId;
}
