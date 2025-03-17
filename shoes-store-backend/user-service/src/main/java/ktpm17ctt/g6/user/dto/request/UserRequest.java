package ktpm17ctt.g6.user.dto.request;

import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ktpm17ctt.g6.user.dto.util.ValidationConstraints;
import ktpm17ctt.g6.user.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "FIRSTNAME_INVALID")
    @NotNull(message = "FIRSTNAME_INVALID")
    @Size(min = 1, max = 50, message = "FIRSTNAME_INVALID")
    @Pattern(regexp = ValidationConstraints.VIETNAMESE_NAME_REGEX, message = "FIRSTNAME_INVALID")
    private String firstName;
    @NotBlank(message = "LASTNAME_INVALID")
    @NotNull(message = "LASTNAME_INVALID")
    @Size(min = 1, max = 50, message = "LASTNAME_INVALID")
    @Pattern(regexp = ValidationConstraints.VIETNAMESE_NAME_REGEX, message = "LASTNAME_INVALID")
    private String lastName;
    @NotBlank(message = "PHONE_INVALID")
    @NotNull(message = "PHONE_INVALID")
    @Size(min = 10, max = 10, message = "PHONE_INVALID")
    @Pattern(regexp = "0[0-9]{9}", message = "PHONE_INVALID")
    private String phone;
    @NotNull(message = "GENDER_INVALID")
    private Gender gender;
}