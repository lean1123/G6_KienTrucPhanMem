package ktpm17ctt.g6.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUrlCreationReq {
    private String orderId;
    private String amount;
    private String ipAddress;
}
