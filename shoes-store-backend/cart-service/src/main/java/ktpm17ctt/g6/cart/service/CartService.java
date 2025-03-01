package ktpm17ctt.g6.cart.service;


import java.util.List;
import java.util.Optional;

import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.enties.Cart;

public interface CartService {
    Optional<Cart> findCartByUser(String userId);  // Optional để tránh NullPointerException
    Cart createCartIfNotExists(String userId);    // Tạo giỏ hàng nếu chưa có
    <S extends Cart> S save(S entity);
    
    void saveCartToSession(String sessionId, CartDetailRequest cartDetailRequest);
    List<CartDetailResponse> getCartFromSession(String sessionId);
    void mergeSessionCartToDatabase(String sessionId, String userId);

}
