package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.PageResponse;
import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.request.ProductItemUpdationRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.dto.response.ProductItemResponseHasLikes;
import ktpm17ctt.g6.product.entity.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductItemService {
    ProductItemResponse save(ProductItemRequest productItemRequest);
    ProductItemResponse update(String id, ProductItemRequest productItemRequest);

    ProductItemResponse updateQuantity(String id, ProductItemUpdationRequest request);

    void delete(String id);
    Optional<ProductItemResponse> findById(String id);

    Optional<ProductItemResponseHasLikes> findProdItemHasLikeById(String id);

    List<ProductItemResponse> findByProductId(String productId);
    PageResponse<ProductItemResponse> search(Integer page, String productName, Type type, String categoryName, String colorName, Integer size, Double minPrice, Double maxPrice);
    int getTotalQuantityByProductAndSize(String id, Integer size);
    PageResponse<ProductItemResponse> newestProductItems(int page, int size);
    List<ProductItemResponse> findAll();

    @Transactional(rollbackFor = Exception.class)
    ProductItemResponse likeProduct(String id);

    ProductItemResponse unlikeProduct(String id);
}
