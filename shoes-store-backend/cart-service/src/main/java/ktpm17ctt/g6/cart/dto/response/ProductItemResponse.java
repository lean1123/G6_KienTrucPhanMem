package ktpm17ctt.g6.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Bỏ qua các field không xác định
public class ProductItemResponse {
    private String id;
    private String productId;
    private String name;
    private double price;
    private List<QuantityOfSizeResponse> quantityOfSize;
}
