package ktpm17ctt.g6.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteCartDetailRequest {
    @NotBlank(message = "ProductItemId must not be blank")
    private String productItemId;

    @Min(value = 1, message = "Size must be at least 1")
    private int size;
}