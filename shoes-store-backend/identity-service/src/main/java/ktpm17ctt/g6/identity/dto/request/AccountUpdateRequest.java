package ktpm17ctt.g6.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUpdateRequest {
    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    String password;
    @Size(min = 6, message = "Password confirm must be at least 6 characters")
    @NotBlank(message = "Password confirm is required")
    String passwordConfirm;
    Set<String> roles;
}
