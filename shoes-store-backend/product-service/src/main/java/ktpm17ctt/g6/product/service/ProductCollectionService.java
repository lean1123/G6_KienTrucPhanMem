package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.request.ProductCollectionRequest;
import ktpm17ctt.g6.product.dto.response.ProductCollectionResponse;

import java.util.List;
import java.util.Optional;

public interface ProductCollectionService {
    ProductCollectionResponse save(ProductCollectionRequest request);
    ProductCollectionResponse update(String id, ProductCollectionRequest request);
    void delete(String id);
    Optional<ProductCollectionResponse> findById(String id);
    List<ProductCollectionResponse> findAll();
    List<ProductCollectionResponse> findAllByBrandId(String brandId);
    List<ProductCollectionResponse> search(String keyword);

}
