package ktpm17ctt.g6.orderservice.dto.feinClient.payment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String orderId;
    double amount;
    String status;
    String transactionId;
    String paymentUrl;
    String transactionDate;
}
