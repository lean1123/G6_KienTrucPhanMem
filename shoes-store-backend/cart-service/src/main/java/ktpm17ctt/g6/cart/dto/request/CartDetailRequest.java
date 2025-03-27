package ktpm17ctt.g6.cart.dto.request;


import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailRequest {
    @Nullable
    private String cartId; 
    private String productItemId; 
    private int quantity;
    int size;
}
