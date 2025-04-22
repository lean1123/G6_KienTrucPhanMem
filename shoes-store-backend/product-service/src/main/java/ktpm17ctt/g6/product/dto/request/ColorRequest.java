package ktpm17ctt.g6.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorRequest {
    @NotBlank(message = "Color name is required")
    @Size(min = 1, max = 50, message = "Color name must be between 1 and 50 characters")
    String name;
    @NotBlank(message = "Color code is required")
    @Size(min = 1, max = 7, message = "Color code must be between 1 and 7 characters")
    String code;
}
