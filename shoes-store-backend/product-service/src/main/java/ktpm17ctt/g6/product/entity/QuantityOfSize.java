package ktpm17ctt.g6.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuantityOfSize {
    @Field("size")
    int size;
    @Field("quantity")
    int quantity;
}
