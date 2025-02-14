package ktpm17ctt.g6.product.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.lang.annotation.Documented;

@Getter
@Setter
@Builder
@Document(value = "product-collection")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCollection {
    @MongoId
    String id;
    String name;
    @DBRef
    Brand brand;
}
