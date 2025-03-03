package ktpm17ctt.g6.product.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import ktpm17ctt.g6.product.entity.enums.Type;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @Size(min = 3, max = 100, message = "PRODUCT_NAME_INVALID")
    String name;
    String description;
    @Min(value = 0, message = "PRODUCT_RATING_INVALID")
    @Max(value = 5, message = "PRODUCT_RATING_INVALID")
    double rating;
    @NotNull(message = "PRODUCT_TYPE_INVALID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Type type;
    @NotNull(message = "PRODUCT_CATEGORY_INVALID")
    @NotEmpty(message = "PRODUCT_CATEGORY_INVALID")
    String categoryId;
    Instant createdDate;
    Instant modifiedDate;
}
