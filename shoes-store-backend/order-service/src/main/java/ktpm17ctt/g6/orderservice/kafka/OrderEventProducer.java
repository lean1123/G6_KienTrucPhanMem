package ktpm17ctt.g6.orderservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderSuccessEvent(String userEmail, String userId) {
        try {
            // Tạo message dạng chuỗi với topic, userEmail, userId
            String message = String.format("order_success_topic|%s|%s", userEmail, userId);
            kafkaTemplate.send("order_success_topic", userEmail, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order success event", e);
        }
    }
}