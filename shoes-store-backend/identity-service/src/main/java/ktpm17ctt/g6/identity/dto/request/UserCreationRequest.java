package ktpm17ctt.g6.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ktpm17ctt.g6.identity.entity.enums.Gender;
import ktpm17ctt.g6.identity.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "Username is required")
    String accountId;
    @NotBlank(message = "First name is required")
    String firstName;
    @NotBlank(message = "Last name is required")
    String lastName;
    @DobConstraint(min = 10, message = "Invalid date of birth")
    LocalDate dob;
    @NotBlank(message = "Phone number is required")
    String phone;
    @NotNull(message = "Gender is required")
    Gender gender;
}

