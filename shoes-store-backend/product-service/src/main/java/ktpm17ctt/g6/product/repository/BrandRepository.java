package ktpm17ctt.g6.product.repository;

import ktpm17ctt.g6.product.entity.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
    Brand findByName(String name);
}
