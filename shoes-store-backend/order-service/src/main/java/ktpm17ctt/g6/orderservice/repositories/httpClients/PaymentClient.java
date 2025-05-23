package ktpm17ctt.g6.orderservice.repositories.httpClients;


import ktpm17ctt.g6.orderservice.dto.feinClient.payment.PaymentResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.payment.RefundResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(name = "payment-service", url = "${PAYMENT_SERVICE_URL:http://localhost:8089/internal/payments}")
public interface PaymentClient {
    @PostMapping("/create")
    ResponseEntity<PaymentResponse> createNewPayment(@RequestParam(required = true) String orderId,
                                                     @RequestParam(required = true) String total,
                                                     @RequestParam(required = true) String ipAddress) throws Exception;

    @PostMapping("/refund")
    ResponseEntity<RefundResponse> refundPayment(
            @RequestParam String orderId,
            @RequestParam String transactionType,
            @RequestParam String amountRequest,
            @RequestParam String user,
            @RequestParam String transDate,
            @RequestParam String ipAddress,
            @RequestParam String transactionNo
    ) throws IOException, Exception;

    @GetMapping("/{orderId}")
    ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId);

}
