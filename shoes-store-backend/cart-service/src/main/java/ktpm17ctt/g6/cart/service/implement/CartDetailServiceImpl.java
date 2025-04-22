package ktpm17ctt.g6.cart.service.implement;

import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.dto.response.ProductItemResponse;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;
import ktpm17ctt.g6.cart.repository.CartDetailRepository;
import ktpm17ctt.g6.cart.repository.CartRepository;
import ktpm17ctt.g6.cart.service.CartDetailService;
import ktpm17ctt.g6.cart.feign.ProductFeignClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.lang.reflect.InvocationTargetException;

@Service
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private ProductFeignClient productFeignClient;

    private ModelMapper modelMapper= new ModelMapper();

    private CartDetail mapToCartDetail(CartDetailRequest cartDetailRequest) {
        return modelMapper.map(cartDetailRequest, CartDetail.class);
    }

    private CartDetailResponse mapToCartDetailResponse(CartDetail cartDetail) {
        return modelMapper.map(cartDetail, CartDetailResponse.class);
    }

    @Override
    public List<CartDetailResponse> findAllCartDetailByCart(String cartId) {
        List<CartDetail> cartDetails= cartDetailRepository.findAllByCartId(cartId);
        return cartDetails.stream().map(this::mapToCartDetailResponse).toList();
    }

    @Override
    public Optional<CartDetailResponse> addToCart(CartDetailRequest cartDetailRequest) {
        CartDetail cartDetail= mapToCartDetail(cartDetailRequest);
        return Optional.of(mapToCartDetailResponse(cartDetailRepository.save(cartDetail)));
    }

    @Override
    public void deleteById(CartDetailPK cartDetailPK) {
        cartDetailRepository.deleteByCartDetailPK(cartDetailPK);
    }

    @Override
    @Transactional
    public Optional<CartDetail> updateQuantity(CartDetailPK cartDetailPK, int newQuantity) {
        Optional<CartDetail> cartDetail = cartDetailRepository.findById(cartDetailPK);
        ApiResponse<ProductItemResponse> productResponse  = productFeignClient.getProductItemById(cartDetailPK.getProductItemId());
        if(productResponse == null || productResponse.getResult()==null){
            throw new RuntimeException("Failed to fetch product details from product-service");
        }

        ApiResponse<Integer> response = productFeignClient.getTotalQuantityByProductItemAndSize(cartDetailPK.getProductItemId(), cartDetailPK.getSize());
        int totalStock=response.getResult();
        if(cartDetail.isPresent()){
            if(newQuantity>totalStock){
                throw new RuntimeException("Not enough stock available");
            }
            cartDetail.get().setQuantity(newQuantity);
            return Optional.of(cartDetailRepository.save(cartDetail.get()));
        }

        return Optional.empty();

    }

    @Override
    public <S extends CartDetail> S save(S entity) {
        return cartDetailRepository.save(entity);
    }

    @Override
    public Optional<CartDetail> findById(CartDetailPK id) {
        return cartDetailRepository.findById(id);
    }

    @Override
    public List<CartDetail> findByCartId(String cartId) {
        return cartDetailRepository.findByCartId(cartId);
    }
    @Override
    public void delete(CartDetail cartDetail) {
        cartDetailRepository.delete(cartDetail);
    }

    @Override
    public void deleteAllByCartId(String cartId) {
        List<CartDetail> cartDetails = cartDetailRepository.findAllByCartId(cartId);
        if (!cartDetails.isEmpty()) {
            cartDetailRepository.deleteAll(cartDetails);
        }
    }
}