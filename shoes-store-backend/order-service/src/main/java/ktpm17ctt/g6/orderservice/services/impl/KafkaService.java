package ktpm17ctt.g6.orderservice.services.impl;

import ktpm17ctt.g6.event.dto.PaymentUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile PaymentUrlResponse paymentUrlResponse;

    public PaymentUrlResponse getPaymentUrlResponse() throws InterruptedException {
        boolean success = latch.await(10, TimeUnit.SECONDS);
        if (!success) {
            log.error("Timeout while waiting for Kafka response.");
            throw new RuntimeException("Payment service timeout.");
        }
        return this.paymentUrlResponse;
    }

    @KafkaListener(topics = "payment-response")
    public void listenPaymentResponse(PaymentUrlResponse paymentUrlResponse) {
        log.info("Kafka response received: {}", paymentUrlResponse);
        this.paymentUrlResponse = paymentUrlResponse;
        latch.countDown(); // Giải phóng chờ đợi
    }
}
