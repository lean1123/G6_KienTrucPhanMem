package ktpm17ctt.g6.cart.service.implement;

import jakarta.servlet.http.HttpSession;
import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.dto.response.ProductItemResponse;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;
import ktpm17ctt.g6.cart.feign.ProductFeignClient;
import ktpm17ctt.g6.cart.repository.CartDetailRepository;
import ktpm17ctt.g6.cart.repository.CartRepository;
import ktpm17ctt.g6.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private ProductFeignClient productFeignClient; // Gọi product-service

    @Autowired
    private HttpSession session;

    @Override
    public Optional<Cart> findCartByUser(String userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Cart createCartIfNotExists(String userId) {
        return findCartByUser(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });
    }

    @Override
    public <S extends Cart> S save(S entity) {
        return cartRepository.save(entity);
    }

    @Override
    public void saveCartToSession(String sessionId, CartDetailRequest cartDetailRequest) {
        List<CartDetailRequest> sessionCart = (List<CartDetailRequest>) session.getAttribute(sessionId);
        if (sessionCart == null) {
            sessionCart = new ArrayList<>();
        }
        sessionCart.add(cartDetailRequest);
        session.setAttribute(sessionId, sessionCart);
    }

    @Override
    public List<CartDetailResponse> getCartFromSession(String sessionId) {
        List<CartDetailRequest> sessionCart = (List<CartDetailRequest>) session.getAttribute(sessionId);
        if (sessionCart == null || sessionCart.isEmpty()) {
            return new ArrayList<>();
        }

        return sessionCart.stream()
            .map(request -> {
                ApiResponse<ProductItemResponse> response = productFeignClient.getProductItemById(request.getProductItemId());
                ProductItemResponse productItem = response != null ? response.getResult() : null;

                return CartDetailResponse.builder()
                    .cartId(null)
                    .productItemId(productItem.getId())
                    .quantity(request.getQuantity())
                    .productId(null)
                    .build();
            })
            .collect(Collectors.toList());
    }


    @Override
    public void mergeSessionCartToDatabase(String sessionId, String userId) {
        Cart cart = createCartIfNotExists(userId);
        List<CartDetailRequest> sessionCart = (List<CartDetailRequest>) session.getAttribute(sessionId);

        if (sessionCart != null) {
            for (CartDetailRequest request : sessionCart) {
                ApiResponse<ProductItemResponse> response = productFeignClient.getProductItemById(request.getProductItemId());
                ProductItemResponse productItem = response != null ? response.getResult() : null;
            	if (productItem == null) {
            	    throw new RuntimeException("Product not found: " + request.getProductItemId());
            	}
                CartDetailPK cartDetailPK = new CartDetailPK(cart.getId(), productItem.getId());
                Optional<CartDetail> existingCartDetail = cartDetailRepository.findById(cartDetailPK);

                if (existingCartDetail.isPresent()) {
                    CartDetail cartDetail = existingCartDetail.get();
                    cartDetail.setQuantity(cartDetail.getQuantity() + request.getQuantity());
                    cartDetailRepository.save(cartDetail);
                } else {
                	CartDetail newCartDetail = CartDetail.builder()
                		    .cartDetailPK(cartDetailPK)
                		    .quantity(request.getQuantity())
                		    .productItemId(productItem.getId())
                		    .cart(cart)
                		    .build();

                    cartDetailRepository.save(newCartDetail);
                }
            }
            session.removeAttribute(sessionId); // Xóa giỏ hàng khỏi session sau khi đã lưu vào database
        }
    }
}
