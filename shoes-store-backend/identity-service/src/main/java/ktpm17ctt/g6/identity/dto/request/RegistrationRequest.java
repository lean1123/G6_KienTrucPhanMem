package ktpm17ctt.g6.identity.dto.request;

import jakarta.validation.constraints.*;
import ktpm17ctt.g6.identity.entity.enums.Gender;
import ktpm17ctt.g6.identity.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    @NotBlank(message = "First name is required")
    String firstName;
    @NotBlank(message = "Last name is required")
    String lastName;

    @DobConstraint(min = 10, message = "Date of birth is invalid")
    LocalDate dob;
    @NotBlank(message = "Phone number is required")
    String phone;
    @NotNull(message = "Gender is required")
    Gender gender;
}
