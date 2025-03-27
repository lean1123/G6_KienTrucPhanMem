package ktpm17ctt.g6.cart.service;


import java.util.List;
import java.util.Optional;

import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.enties.Cart;

public interface CartService {
    Cart findCartByUser(String userId);  // Optional để tránh NullPointerException
    Cart saveSessionItemsToDatabase(String sessionId, String userId);
    <S extends Cart> S save(S entity);
    void addToCart(String cartId, String productItemId, int size, int quantity);

}
