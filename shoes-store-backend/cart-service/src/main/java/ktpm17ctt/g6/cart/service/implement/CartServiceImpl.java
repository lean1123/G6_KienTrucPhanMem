package ktpm17ctt.g6.cart.service.implement;

import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.product.ProductItemResponse;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;
import ktpm17ctt.g6.cart.feign.ProductFeignClient;
import ktpm17ctt.g6.cart.repository.CartDetailRepository;
import ktpm17ctt.g6.cart.repository.CartRepository;
import ktpm17ctt.g6.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private ProductFeignClient productFeignClient; // Gọi product-service

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public Cart findCartByUser(String userId) {
        return  cartRepository.findByUserId(userId);
    }

    @Override
    public Cart saveSessionItemsToDatabase(String sessionId, String userId) {
        List<ProductItemResponse> sessionItems = getSessionItems(sessionId);
        return null;
    }

    @Override
    public <S extends Cart> S save(S entity) {
        return cartRepository.save(entity);
    }

    private List<ProductItemResponse> getSessionItems(String sessionId) {
        ApiResponse<List<ProductItemResponse>> response = productFeignClient.getAllProductItems();

        if (response.getResult() != null) {
            return response.getResult();
        }

        return List.of(); // Trả về danh sách rỗng nếu không có dữ liệu
    }

    @Override
    @Transactional
    public void addToCart(String cartId, String productItemId, int size, int quantity) {
        CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId, size);
        Optional<CartDetail> existingCartDetail = cartDetailRepository.findById(cartDetailPK);

        if (existingCartDetail.isPresent()) {
            // Nếu sản phẩm đã tồn tại, cập nhật số lượng
            CartDetail cartDetail = existingCartDetail.get();
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
            cartDetailRepository.save(cartDetail);
        } else {
            // Nếu sản phẩm chưa có, thêm mới vào giỏ hàng
            CartDetail newCartDetail = new CartDetail(cartDetailPK, quantity);
            cartDetailRepository.save(newCartDetail);
        }
    }

}