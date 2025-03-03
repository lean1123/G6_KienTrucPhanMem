package ktpm17ctt.g6.paymentservice.controllers.internals;

import jakarta.servlet.http.HttpServletRequest;
import ktpm17ctt.g6.paymentservice.configurations.VNPayConfig;
import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PaymentInternalController {
    PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createNewPayment(@RequestParam(required = true) String orderId,
                                            @RequestParam(required = true) String total,
                                            @RequestParam(required = true) String ipAddress,
                                            @RequestParam (required = false) String bankCode,
                                            @RequestParam (required = false) String language
                                            ) throws Exception {
        return  ResponseEntity.ok(
                PaymentResponse.builder()
                        .paymentUrl(VNPayConfig.getPaymentUrl(orderId, total, ipAddress, bankCode, language))
                        .build()
        );

    }

    @GetMapping("/status")
    public ResponseEntity<PaymentResponse> getPaymentStatus(
            @RequestParam(required = true) String vnp_Amount,
            @RequestParam(required = true) String vnp_BankCode,
            @RequestParam(required = true) String vnp_PayDate,
            @RequestParam(required = true) String vnp_ResponseCode,
            @RequestParam(required = true) String vnp_TransactionNo,
            @RequestParam(required = true) String vnp_TransactionStatus,
            @RequestParam(required = true) String vnp_TxnRef
    ) throws IOException {
        return ResponseEntity.ok(
                paymentService.save(vnp_TxnRef, vnp_TransactionNo, vnp_ResponseCode, vnp_Amount)
        );
    }
}
