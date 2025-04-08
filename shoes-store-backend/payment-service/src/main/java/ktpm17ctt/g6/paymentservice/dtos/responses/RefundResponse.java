package ktpm17ctt.g6.paymentservice.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {
    private String orderId;
    private String transactionId;
    private String vnp_ResponseCode;
    private String vnp_Message;
    private String transactionType;
    private String amountRequest;
}
