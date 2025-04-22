package ktpm17ctt.g6.orderservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderSuccessEvent(String userEmail) {
        String message = "Order successfully placed for user: " + userEmail;
        kafkaTemplate.send("order_success_topic", userEmail, message); // Send to a Kafka topic
    }
}
