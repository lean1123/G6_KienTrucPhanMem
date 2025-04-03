package ktpm17ctt.g6.paymentservice.services.impl;

import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.entities.Payment;
import ktpm17ctt.g6.paymentservice.entities.PaymentStatus;
import ktpm17ctt.g6.paymentservice.repositories.PaymentRepository;
import ktpm17ctt.g6.paymentservice.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;
    KafkaTemplate<String, String> kafkaTemplate; // KafkaTemplate để gửi sự kiện
    private static final String PAYMENT_SUCCESS_TOPIC = "payment_success"; // Định nghĩa topic Kafka

    @Transactional
    @Override
    public PaymentResponse save(String orderId, String transactionId, String responseCode, String amount, String transDate, String userEmail) {
        Payment paymentEntity = Payment.builder()
                .orderId(orderId)
                .transactionId(transactionId)
                .status(responseCode.equals("00") ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .amount(Long.parseLong(amount))
                .transactionDate(transDate)
                .build();

        paymentEntity = paymentRepository.save(paymentEntity);


        // Gửi sự kiện Kafka nếu thanh toán thành công
        if (responseCode.equals("00")) {
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .orderId(paymentEntity.getOrderId())
                    .status(String.valueOf(paymentEntity.getStatus()))
                    .transactionId(paymentEntity.getTransactionId())
                    .amount(paymentEntity.getAmount())
                    .userEmail(userEmail)  // Thêm thông tin email người dùng
                    .build();

            // Gửi sự kiện Kafka với thông tin thanh toán thành công
            kafkaTemplate.send(PAYMENT_SUCCESS_TOPIC, paymentResponse.getUserEmail());
        }
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
