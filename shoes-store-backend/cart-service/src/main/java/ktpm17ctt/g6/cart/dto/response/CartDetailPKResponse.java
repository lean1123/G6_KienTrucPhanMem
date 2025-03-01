package ktpm17ctt.g6.cart.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartDetailPKResponse {
    private String cartId;
    private String productItemId;
}
