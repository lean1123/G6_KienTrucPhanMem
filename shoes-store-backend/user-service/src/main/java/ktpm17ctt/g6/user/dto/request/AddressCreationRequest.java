package ktpm17ctt.g6.user.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressCreationRequest {

    @Nullable
    private String id;

    @NotBlank(message = "Home number must not be blank")
    @Size(max = 100, message = "Home number must be less than or equal to 100 characters")
    private String homeNumber;

    @NotBlank(message = "Ward must not be blank")
    @Size(max = 100, message = "Ward must be less than or equal to 100 characters")
    private String ward;

    @NotBlank(message = "District must not be blank")
    @Size(max = 100, message = "District must be less than or equal to 100 characters")
    private String district;

    @NotBlank(message = "City must not be blank")
    @Size(max = 100, message = "City must be less than or equal to 100 characters")
    private String city;

    @NotBlank(message = "User ID must not be blank")
    private String userId;
}
