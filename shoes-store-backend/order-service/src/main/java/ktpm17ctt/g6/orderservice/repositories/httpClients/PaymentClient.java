package ktpm17ctt.g6.orderservice.repositories.httpClients;

import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.commondto.dtos.responses.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", url = "http://localhost:8089/internal/payments")
public interface PaymentClient {
    @PostMapping("/create")
    ResponseEntity<PaymentResponse> createNewPayment(@RequestParam(required = true) String orderId,
                                                     @RequestParam(required = true) String total,
                                                     @RequestParam(required = true) String ipAddress) throws Exception;

}
