package iuh.fit.se.chatService.dtos;

import iuh.fit.se.chatService.dtos.product.ProductItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AIResponse {
    List<ProductItemResponse> results;
    String message;
    String intent;
}
