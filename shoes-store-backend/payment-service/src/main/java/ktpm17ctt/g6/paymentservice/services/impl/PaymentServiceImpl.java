package ktpm17ctt.g6.paymentservice.services.impl;

import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.entities.Payment;
import ktpm17ctt.g6.paymentservice.entities.PaymentStatus;
import ktpm17ctt.g6.paymentservice.repositories.PaymentRepository;
import ktpm17ctt.g6.paymentservice.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;

    @Transactional
    @Override
    public PaymentResponse save(String orderId, String transactionId, String responseCode, String amount, String transDate) {
        Payment paymentEntity = Payment.builder()
                .orderId(orderId)
                .transactionId(transactionId)
                .status(responseCode.equals("00") ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .amount(Long.parseLong(amount))
                .transactionDate(transDate)
                .build();

        paymentEntity = paymentRepository.save(paymentEntity);
        return PaymentResponse.builder()
                .orderId(paymentEntity.getOrderId())
                .status(String.valueOf(paymentEntity.getStatus()))
                .transactionId(paymentEntity.getTransactionId())
                .amount(paymentEntity.getAmount())
                .build();
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment paymentEntity = paymentRepository.findByOrderId(orderId).orElseThrow(() ->
                new RuntimeException("Payment not found"));

        return PaymentResponse.builder()
                .orderId(paymentEntity.getOrderId())
                .status(String.valueOf(paymentEntity.getStatus()))
                .transactionId(paymentEntity.getTransactionId())
                .amount(paymentEntity.getAmount())
                .build();
    }


}
