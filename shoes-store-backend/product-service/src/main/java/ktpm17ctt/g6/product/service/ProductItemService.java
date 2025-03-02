package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;

import java.util.List;
import java.util.Optional;

public interface ProductItemService {
    ProductItemResponse save(ProductItemRequest productItemRequest);
    ProductItemResponse update(String id, ProductItemRequest productItemRequest);
    void delete(String id);
    Optional<ProductItemResponse> findById(String id);
    List<ProductItemResponse> findByProductId(String productId);
}
