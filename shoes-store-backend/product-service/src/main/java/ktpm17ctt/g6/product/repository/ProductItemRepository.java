package ktpm17ctt.g6.product.repository;

import ktpm17ctt.g6.product.entity.ProductItem;
import ktpm17ctt.g6.product.repository.custom.CustomProductItemRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends MongoRepository<ProductItem, String>, CustomProductItemRepository {
    List<ProductItem> findByProduct_Id(String productId);
}
