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
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    String name;
    String description;
    @Min(value = 0, message = "Product price must be greater than 0")
    @Max(value = 5, message = "Product price must be less than 5")
    double rating;
    @NotNull(message = "Product type is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Type type;
    @NotBlank(message = "Product category id is required")
    String categoryId;

    Instant createdDate;
    Instant modifiedDate;
}
