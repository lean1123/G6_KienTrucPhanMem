package ktpm17ctt.g6.cart.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProductItemResponse {
    private String id;
    private String productId;
    private String name;
    private double price;
    private List<QuantityOfSizeResponse> quantityOfSize;
}
