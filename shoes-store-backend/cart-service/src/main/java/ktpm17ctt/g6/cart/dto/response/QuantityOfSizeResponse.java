package ktpm17ctt.g6.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuantityOfSizeResponse {
    private Integer size;
    private Integer quantity;
}
