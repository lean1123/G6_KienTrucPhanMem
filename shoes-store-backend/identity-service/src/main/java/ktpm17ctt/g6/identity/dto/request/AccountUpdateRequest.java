package ktpm17ctt.g6.identity.dto.request;

import ktpm17ctt.g6.identity.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUpdateRequest {
    String password;
    String passwordConfirm;
    Set<String> roles;
}
