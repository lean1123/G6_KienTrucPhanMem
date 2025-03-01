package ktpm17ctt.g6.cart.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuantityOfSizeResponse {
    private Integer size;
    private Integer quantity;
}
