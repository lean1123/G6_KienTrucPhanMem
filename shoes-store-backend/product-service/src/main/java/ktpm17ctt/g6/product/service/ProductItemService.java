package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.PageResponse;
import ktpm17ctt.g6.product.dto.request.ProductItemRequest;
import ktpm17ctt.g6.product.dto.response.ProductItemResponse;
import ktpm17ctt.g6.product.entity.enums.Type;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductItemService {
    ProductItemResponse save(ProductItemRequest productItemRequest);
    ProductItemResponse update(String id, ProductItemRequest productItemRequest);
    void delete(String id);
    Optional<ProductItemResponse> findById(String id);
    List<ProductItemResponse> findByProductId(String productId);
    PageResponse<ProductItemResponse> search(Integer page, String productName, Type type, String categoryName, String colorName, Integer size, Double minPrice, Double maxPrice);
    int getTotalQuantityByProductAndSize(String id, Integer size);
    PageResponse<ProductItemResponse> newestProductItems(int page, int size);
    List<ProductItemResponse> findAll();
}
