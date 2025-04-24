package ktpm17ctt.g6.notification.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ktpm17ctt.g6.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;

@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final EmailService emailService;

    public OrderEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order_success_topic", groupId = "notification-group")
    public void listenOrderSuccessEvent(String message, Acknowledgment acknowledgment) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(message);
            if (!json.has("userEmail")) {
                log.error("Missing userEmail field in message: {}", message);
                acknowledgment.acknowledge();
                return;
            }
            String userEmail = json.get("userEmail").asText();
            if (!isValidEmail(userEmail)) {
                log.error("Invalid email format: {}", userEmail);
                acknowledgment.acknowledge();
                return;
            }
            String userId = json.has("userId") ? json.get("userId").asText() : "unknown"; // Lấy userId hoặc mặc định
            String subject = "Order Confirmation";
            String content = "Your order has been successfully placed.";
            emailService.sendEmail(userEmail, subject, content, userId); // Thêm userId
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", message, e);
            throw new RuntimeException("Failed to process message", e);
        }
    }

    private boolean isValidEmail(String email) {
        try {
            new InternetAddress(email).validate();
            return true;
        } catch (javax.mail.internet.AddressException e) {
            return false;
        }
    }
}