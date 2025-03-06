package ktpm17ctt.g6.product.repository.custom;

import ktpm17ctt.g6.product.entity.ProductItem;
import ktpm17ctt.g6.product.entity.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductItemRepository {
    Page<ProductItem> search(String productName, Type type, String categoryName, String colorName, Integer size, Double minPrice, Double maxPrice, Pageable pageable);
}

