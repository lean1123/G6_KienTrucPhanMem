package ktpm17ctt.g6.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ktpm17ctt.g6.product.entity.Color;
import ktpm17ctt.g6.product.entity.QuantityOfSize;
import ktpm17ctt.g6.product.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItemRequest {
    @Positive(message = "ITEM_PRICE_INVALID")
    double price;
    List<String> images;
    @NotNull(message = "ITEM_COLOR_INVALID")
    String colorId;
    List<QuantityOfSize> quantityOfSize;
    @NotNull(message = "ITEM_PRODUCT_INVALID")
    String productId;
    Status status;
}
