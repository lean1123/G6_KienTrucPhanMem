package ktpm17ctt.g6.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUrlResponse {
    private String paymentUrl;
    private String status;
    private String message;
}
