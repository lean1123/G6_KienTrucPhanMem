package ktpm17ctt.g6.product.repository;

import ktpm17ctt.g6.product.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByNameContainingIgnoreCase(String name);
}
