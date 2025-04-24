package ktpm17ctt.g6.cart.dto.request;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDetailPKRequest {
    private String cartId;
    private String productItemId;
    private int size;
}
