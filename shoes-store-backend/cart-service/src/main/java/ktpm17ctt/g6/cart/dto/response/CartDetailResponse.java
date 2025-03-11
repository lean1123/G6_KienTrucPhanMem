package ktpm17ctt.g6.cart.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDetailResponse {
    private String cartId;
    private String productItemId;
    private int quantity;
    private String productId; 
    private int size;
}
