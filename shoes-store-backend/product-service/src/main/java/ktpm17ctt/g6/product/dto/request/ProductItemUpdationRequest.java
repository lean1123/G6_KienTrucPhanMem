package ktpm17ctt.g6.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ktpm17ctt.g6.product.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItemUpdationRequest {
    @Nullable
    String id;
    @NotNull(message = "Product item price is required")
    @Positive(message = "Product item price must be positive")
    double price;
    List<String> images;
    @NotBlank(message = "Product item color is required")
    String colorId;
    @NotBlank(message = "Product item quantity of size is required")
    String quantityOfSize;
    @NotBlank(message = "Product item product id is required")
    String productId;
    @NotNull(message = "Product item status is required")
    Status status;
}
