package ktpm17ctt.g6.orderservice.repositories.httpClients;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", url = "http://localhost:8089/internal/payments")
public interface PaymentClient {
    @PostMapping("/create")
    ResponseEntity<?> createNewPayment(@RequestParam(required = true) String orderId,
                                       @RequestParam(required = true) String total,
                                       HttpServletRequest req) throws Exception;
}
