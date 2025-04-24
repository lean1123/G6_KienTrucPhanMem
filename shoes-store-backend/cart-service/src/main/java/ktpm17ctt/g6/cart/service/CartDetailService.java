package ktpm17ctt.g6.cart.service;




import java.util.List;
import java.util.Optional;

import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;

public interface CartDetailService {
    List<CartDetailResponse> findAllCartDetailByCart(String cartId);
    Optional<CartDetailResponse> addToCart(CartDetailRequest cartDetailRequest);
    void deleteById(CartDetailPK cartDetailPK);
    Optional<CartDetail> updateQuantity(CartDetailPK cartDetailPK, int newQuantity);
    <S extends CartDetail> S save(S entity);
    Optional<CartDetail> findById(CartDetailPK id);

    List<CartDetail> findByCartId(String cartId);
    void delete(CartDetail cartDetail);
    void deleteAllByCartId(String cartId);
}
