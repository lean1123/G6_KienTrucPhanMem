package ktpm17ctt.g6.paymentservice.controllers;

import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PaymentController {
    PaymentService paymentService;

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}
