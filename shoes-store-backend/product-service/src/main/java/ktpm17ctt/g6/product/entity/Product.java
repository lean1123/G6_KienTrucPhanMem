package ktpm17ctt.g6.product.entity;

import ktpm17ctt.g6.product.entity.enums.Type;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@Builder
@Document(value = "product")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @MongoId
    String id;
    String code;
    String name;
    String description;
    double rating;
    @Field("type")
    Type type;
    @Field("category")
    Category category;
    @CreatedDate()
    Instant createdDate;
    @LastModifiedDate()
    Instant modifiedDate;
}
