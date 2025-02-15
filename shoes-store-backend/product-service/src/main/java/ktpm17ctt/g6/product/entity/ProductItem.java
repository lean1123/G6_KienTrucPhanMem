package ktpm17ctt.g6.product.entity;

import ktpm17ctt.g6.product.entity.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@Builder
@Document(value = "product-item")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItem {
    @MongoId
    String id;
    double price;
    List<String> images;
    @DBRef
    Color color;
    @Field("quantityOfSize")
    List<QuantityOfSize> quantityOfSize;
    @DBRef
    Product product;
    @Field("status")
    Status status;
}
