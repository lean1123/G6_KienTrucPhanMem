package ktpm17ctt.g6.product.repository;

import ktpm17ctt.g6.product.entity.ProductCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCollectionRepository extends MongoRepository<ProductCollection, String> {
    ProductCollection findByName(String name);
    List<ProductCollection> findByBrand_Id(String brandId);
}
