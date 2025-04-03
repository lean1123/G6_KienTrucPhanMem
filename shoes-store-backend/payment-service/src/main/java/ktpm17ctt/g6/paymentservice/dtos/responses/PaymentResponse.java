package ktpm17ctt.g6.paymentservice.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String orderId;
    long amount;
    String status;
    String transactionId;
    String paymentUrl;
    private String userEmail;  // Thêm thông tin email người dùng

}
