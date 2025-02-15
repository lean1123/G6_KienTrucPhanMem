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
@Document(value = "color")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Color {
    @MongoId
    String id;
    String name;
    String code;
}
