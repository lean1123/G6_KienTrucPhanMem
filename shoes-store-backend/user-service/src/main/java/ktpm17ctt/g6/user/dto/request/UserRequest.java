package ktpm17ctt.g6.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ktpm17ctt.g6.user.util.ValidationConstraints;
import ktpm17ctt.g6.user.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Account ID is required")
    private String accountId;
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(regexp = ValidationConstraints.VIETNAMESE_NAME_REGEX, message = "First name must be valid")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Pattern(regexp = ValidationConstraints.VIETNAMESE_NAME_REGEX, message = "Last name must be valid")
    private String lastName;
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;
    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 10, message = "Phone must be 10 characters")
    @Pattern(regexp = "0[0-9]{9}", message = "Phone must be valid")
    private String phone;
    @NotNull(message = "Gender is required")
    private Gender gender;
}