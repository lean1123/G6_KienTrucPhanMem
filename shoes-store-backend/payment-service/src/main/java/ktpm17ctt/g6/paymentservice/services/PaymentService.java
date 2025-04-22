package ktpm17ctt.g6.paymentservice.services;

import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.dtos.responses.RefundResponse;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {
    @Transactional
    PaymentResponse save(String orderId, String transactionId, String responseCode, String amount, String transDate);

    PaymentResponse getPaymentByOrderId(String orderId);

    RefundResponse refundPayment(
            String orderId,
            String transactionType,
            String amountRequest,
            String user,
            String transDate,
            String ipAddress,
            String transactionNo
    ) throws Exception;
}
