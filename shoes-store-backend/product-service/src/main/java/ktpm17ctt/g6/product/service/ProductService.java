package ktpm17ctt.g6.product.service;

import ktpm17ctt.g6.product.dto.request.ProductRequest;
import ktpm17ctt.g6.product.dto.response.ProductResponse;
import ktpm17ctt.g6.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse save(ProductRequest productRequest);
    ProductResponse update(String id, ProductRequest productRequest);
    void delete(String id);
    Optional<ProductResponse> findById(String id);
    List<ProductResponse> findAll();
}
