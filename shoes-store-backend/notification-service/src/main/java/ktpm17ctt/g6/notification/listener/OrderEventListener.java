package ktpm17ctt.g6.notification.listener;

import ktpm17ctt.g6.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

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
            if (message == null || !message.contains(":")) {
                log.error("Invalid message format: {}", message);
                acknowledgment.acknowledge();
                return;
            }

            String userEmail = message.split(":")[1].trim();
            if (!isValidEmail(userEmail)) {
                log.error("Invalid email format: {}", userEmail);
                acknowledgment.acknowledge();
                return;
            }

            String subject = "Order Confirmation";
            String content = "Your order has been successfully placed.";
            emailService.sendEmail(userEmail, subject, content);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", message, e);
            // Có thể gửi message sang DLQ thay vì acknowledge ngay
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}