package ktpm17ctt.g6.product.dto.request;

import jakarta.validation.constraints.Size;
import ktpm17ctt.g6.product.exception.ErrorCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    @Size(min = 3, max = 50, message = "CATEGORY_NAME_INVALID")
    String name;
}
