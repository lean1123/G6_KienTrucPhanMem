package ktpm17ctt.g6.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import ktpm17ctt.g6.identity.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    @Email(message = "INVALID_EMAIL")
    String email;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dob;

    String city;
}
