package ktpm17ctt.g6.paymentservice.controllers.internals;

import jakarta.servlet.http.HttpServletResponse;
import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.dtos.responses.RefundResponse;
import ktpm17ctt.g6.paymentservice.services.PaymentService;
import ktpm17ctt.g6.paymentservice.services.VNPayService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentInternalController {
    private final PaymentService paymentService;
    private final VNPayService vnPayService;

    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createNewPayment(@RequestParam(required = true) String orderId,
                                                            @RequestParam(required = true) String total,
                                                            @RequestParam(required = true) String ipAddress,
                                                            @RequestParam(required = false) String bankCode,
                                                            @RequestParam(required = false) String language
    ) throws Exception {
        return ResponseEntity.ok(
                PaymentResponse.builder()
                        .paymentUrl(vnPayService.getPaymentUrl(orderId, total, ipAddress, bankCode, language))
                        .build()
        );

    }

    @GetMapping("/status")
    public void getPaymentStatus(
            @RequestParam(required = true) String vnp_Amount,
            @RequestParam(required = true) String vnp_BankCode,
            @RequestParam(required = true) String vnp_PayDate,
            @RequestParam(required = true) String vnp_ResponseCode,
            @RequestParam(required = true) String vnp_TransactionNo,
            @RequestParam(required = true) String vnp_TransactionStatus,
            @RequestParam(required = true) String vnp_TxnRef,
            HttpServletResponse httpServletResponse
    ) throws IOException {

        paymentService.save(vnp_TxnRef, vnp_TransactionNo, vnp_ResponseCode, vnp_Amount, vnp_PayDate);

        httpServletResponse.sendRedirect(FRONTEND_URL+"/vnpayOrderResult");
    }

    @PostMapping("/refund")
    public ResponseEntity<RefundResponse> refundPayment(
            @RequestParam String orderId,
            @RequestParam String transactionType,
            @RequestParam String amountRequest,
            @RequestParam String user,
            @RequestParam String transDate,
            @RequestParam String ipAddress,
            @RequestParam String transactionNo
    ) throws Exception {
        return ResponseEntity.ok(paymentService.refundPayment(orderId, transactionType, amountRequest, user, transDate, ipAddress, transactionNo));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}
