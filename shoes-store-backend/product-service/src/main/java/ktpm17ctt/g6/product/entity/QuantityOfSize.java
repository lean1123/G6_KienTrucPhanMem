package ktpm17ctt.g6.product.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "SIZE_NOT_NULL")
    @Min(value = 30, message = "SIZE_INVALID")
    @Max(value = 50, message = "SIZE_INVALID")
    Integer size;
    @Field("quantity")
    @NotNull(message = "QUANTITY_NOT_NULL")
    Integer quantity;
}
