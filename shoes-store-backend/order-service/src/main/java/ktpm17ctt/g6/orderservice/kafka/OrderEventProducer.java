package ktpm17ctt.g6.orderservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;



    public void sendOrderSuccessEvent(String userEmail, String userId) {
        try {
            Map<String, String> message = new HashMap<>();
            message.put("userEmail", userEmail);
            message.put("userId", userId);
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("order_success_topic", userEmail, jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send order success event", e);
        }
    }
}
