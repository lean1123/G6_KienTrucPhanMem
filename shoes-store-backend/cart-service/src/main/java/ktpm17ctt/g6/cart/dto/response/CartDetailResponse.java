package ktpm17ctt.g6.cart.dto.response;


import ktpm17ctt.g6.cart.dto.response.product.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.product.ProductItemResponseHasLikes;
import ktpm17ctt.g6.cart.enties.CartDetailPK;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDetailResponse {
    private CartDetailPK cartDetailPK;
    private ProductItemResponseHasLikes productItem;
    private int quantity;
//    private String productId;
}
