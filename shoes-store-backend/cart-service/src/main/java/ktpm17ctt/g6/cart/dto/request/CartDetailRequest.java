package ktpm17ctt.g6.cart.dto.request;


//import jakarta.annotation.Nullable;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class CartDetailRequest {
//    @Nullable
//    private String cartId;
//    private String productItemId;
//    private int quantity;
//    int size;
//}
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailRequest {
    private String cartId; // Nullable, không cần validation
    @NotBlank(message = "ProductItemId must not be blank")
    private String productItemId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    @NotNull(message = "Size must not be null")
    @Min(value = 1, message = "Size must be at least 1")
    private Integer size; // Chuyển sang Integer để hỗ trợ @NotNull
}