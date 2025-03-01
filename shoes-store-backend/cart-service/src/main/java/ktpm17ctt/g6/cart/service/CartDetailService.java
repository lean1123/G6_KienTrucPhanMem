package ktpm17ctt.g6.cart.service;




import java.util.List;
import java.util.Optional;

import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;

public interface CartDetailService {
    List<CartDetailResponse> findAllByCartId(String cartId);
    Optional<CartDetailResponse> addToCart(CartDetailRequest cartDetailRequest);
    void deleteByCartIdAndProductId(String cartId, String productItemId);
    Optional<CartDetailResponse> updateQuantityByCartId(String cartId, String productItemId, int newQuantity);
    boolean removeCartDetail(String cartId, String productItemId);

}
