package ktpm17ctt.g6.product.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Builder
@Document(value = "brand")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand {
    @MongoId
    String id;
    String name;
    String avatar;
}
