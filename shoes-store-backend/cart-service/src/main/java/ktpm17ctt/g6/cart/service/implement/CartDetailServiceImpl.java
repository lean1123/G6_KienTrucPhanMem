package ktpm17ctt.g6.cart.service.implement;

import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;
import ktpm17ctt.g6.cart.repository.CartDetailRepository;
import ktpm17ctt.g6.cart.repository.CartRepository;
import ktpm17ctt.g6.cart.service.CartDetailService;
import ktpm17ctt.g6.cart.feign.ProductFeignClient;
import ktpm17ctt.g6.cart.dto.response.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.QuantityOfSizeResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public List<CartDetailResponse> findAllByCartId(String cartId) {
        List<CartDetail> cartDetails = cartDetailRepository.findByCart_Id(cartId);
        return cartDetails.stream().map(cartDetail -> 
            CartDetailResponse.builder()
                .cartId(cartDetail.getCart().getId())
                .productItemId(cartDetail.getProductItemId())
                .quantity(cartDetail.getQuantity())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<CartDetailResponse> addToCart(CartDetailRequest cartDetailRequest) {
        String cartId = cartDetailRequest.getCartId();
        String productItemId = cartDetailRequest.getProductItemId();
        int quantity = cartDetailRequest.getQuantity();
        
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId);
        Optional<CartDetail> existingCartDetail = cartDetailRepository.findById(cartDetailPK);

        if (existingCartDetail.isPresent()) {
            CartDetail cartDetail = existingCartDetail.get();
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
            cartDetailRepository.save(cartDetail);
            return Optional.of(toResponse(cartDetail));
        }

        CartDetail newCartDetail = CartDetail.builder()
            .cartDetailPK(cartDetailPK)
            .quantity(quantity)
            .productItemId(productItemId)
            .cart(cart)
            .build();

        cartDetailRepository.save(newCartDetail);
        return Optional.of(toResponse(newCartDetail));
    }


    @Override
    public void deleteByCartIdAndProductId(String cartId, String productItemId) {
        CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId);
        cartDetailRepository.deleteById(cartDetailPK);
    }

//    @Override
//    public Optional<CartDetailResponse> updateQuantityByCartId(String cartId, String productItemId, int newQuantity) {
//        CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId);
//        Optional<CartDetail> existingCartDetail = cartDetailRepository.findById(cartDetailPK);
//
//        if (existingCartDetail.isPresent()) {
//            CartDetail cartDetail = existingCartDetail.get();
//            cartDetail.setQuantity(newQuantity);
//            cartDetailRepository.save(cartDetail);
//            return Optional.of(toResponse(cartDetail));
//        }
//        return Optional.empty();
//    }
    
    
//    @Override
//    public Optional<CartDetailResponse> updateQuantityByCartId(String cartId, String productItemId, int newQuantity) {
//        CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId);
//        Optional<CartDetail> existingCartDetail = cartDetailRepository.findById(cartDetailPK);
//
//        if (existingCartDetail.isPresent()) {
//            ProductItemResponse productItem = productFeignClient.getProductItemById(productItemId);
//            if (productItem.getStock() < newQuantity) {
//                throw new RuntimeException("Not enough stock available");
//            }
//
//            CartDetail cartDetail = existingCartDetail.get();
//            cartDetail.setQuantity(newQuantity);
//            cartDetailRepository.save(cartDetail);
//            return Optional.of(toResponse(cartDetail));
//        }
//        return Optional.empty();
//    }


//    @Override
//    public Optional<CartDetailResponse> updateQuantityByCartId(String cartId, String productItemId, int newQuantity) {
//        CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId);
//        Optional<CartDetail> existingCartDetail = cartDetailRepository.findById(cartDetailPK);
//
//        if (existingCartDetail.isPresent()) {
//            CartDetail cartDetail = existingCartDetail.get();
//
//            // Gọi product-service để lấy danh sách tồn kho theo size
//            List<QuantityOfSizeResponse> quantityOfSizes = productFeignClient.getQuantityOfSize(productItemId);
//
//            // Kiểm tra tổng tồn kho của tất cả sizes
//            int totalStock = quantityOfSizes.stream().mapToInt(QuantityOfSizeResponse::getQuantity).sum();
//
//            if (totalStock < newQuantity) {
//                throw new RuntimeException("Not enough stock available");
//            }
//
//            // Cập nhật số lượng trong giỏ hàng
//            cartDetail.setQuantity(newQuantity);
//            cartDetailRepository.save(cartDetail);
//            return Optional.of(toResponse(cartDetail));
//        }
//        return Optional.empty();
//    }
@Override
public Optional<CartDetailResponse> updateQuantityByCartId(String cartId, String productItemId, int newQuantity) {
    CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId);
    Optional<CartDetail> existingCartDetail = cartDetailRepository.findById(cartDetailPK);

    if (existingCartDetail.isPresent()) {
        CartDetail cartDetail = existingCartDetail.get();

        // Giả sử giỏ hàng có lưu size, nếu không có thì cần thêm size vào CartDetail
        int size = cartDetail.getSize();

        // Gọi product-service để lấy tổng tồn kho theo size
        ApiResponse<Integer> response = productFeignClient.getTotalQuantityByProductItemAndSize(productItemId, size);

        // Kiểm tra nếu response null hoặc không có dữ liệu hợp lệ
        if (response == null || response.getResult() == null) {
            throw new RuntimeException("Failed to fetch stock information from product-service");
        }

        int totalStock = response.getResult();

        if (totalStock < newQuantity) {
            throw new RuntimeException("Not enough stock available");
        }

        // Cập nhật số lượng trong giỏ hàng
        cartDetail.setQuantity(newQuantity);
        cartDetailRepository.save(cartDetail);
        return Optional.of(toResponse(cartDetail));
    }
    return Optional.empty();
}

    
    private CartDetailResponse toResponse(CartDetail cartDetail) {
        return CartDetailResponse.builder()
                .cartId(cartDetail.getCart().getId())
                .productItemId(cartDetail.getProductItemId())
                .quantity(cartDetail.getQuantity())
                .build();
    }
    @Override
    public boolean removeCartDetail(String cartId, String productItemId) {
        CartDetailPK cartDetailPK = new CartDetailPK(cartId, productItemId);
        if (cartDetailRepository.existsById(cartDetailPK)) {
            cartDetailRepository.deleteById(cartDetailPK);
            return true;
        }
        return false;
    }

}