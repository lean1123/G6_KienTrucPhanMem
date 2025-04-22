package ktpm17ctt.g6.identity.dto.response;

import ktpm17ctt.g6.identity.entity.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    String id;
    String firstName;
    String lastName;
    LocalDate dob;
    String phone;
    String avatar;
    Gender gender;
}
