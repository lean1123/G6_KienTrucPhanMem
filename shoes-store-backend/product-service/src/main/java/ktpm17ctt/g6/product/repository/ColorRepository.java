package ktpm17ctt.g6.product.repository;

import ktpm17ctt.g6.product.entity.Color;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends MongoRepository<Color, String> {
    @Query("{'$or': [ {'name': {$regex: ?0, $options: 'i'}}, {'code': {$regex: ?0, $options: 'i'}} ] }")
    List<Color> searchByNameOrCode(String keyString);
}
