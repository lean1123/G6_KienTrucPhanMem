package ktpm17ctt.g6.notification.listener;

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
            // Parse message dạng topic|userEmail|userId
            String[] parts = message.split("\\|");
            if (parts.length != 3) {
                log.error("Invalid message format: {}", message);
                acknowledgment.acknowledge();
                return;
            }

            String topic = parts[0];
            String userEmail = parts[1];
            String userId = parts[2];

            if (!isValidEmail(userEmail)) {
                log.error("Invalid email format: {}", userEmail);
                acknowledgment.acknowledge();
                return;
            }

            String subject = "Order Confirmation";
            String content = "Your order has been successfully placed.";
            emailService.sendEmail(userEmail, subject, content, userId);
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