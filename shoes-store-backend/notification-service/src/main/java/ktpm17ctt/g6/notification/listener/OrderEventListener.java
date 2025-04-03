package ktpm17ctt.g6.notification.listener;

import ktpm17ctt.g6.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final EmailService emailService;

    public OrderEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order_success_topic", groupId = "notification-group")
    public void listenOrderSuccessEvent(String message) {
        String userEmail = message.split(":")[1].trim(); // Lấy email từ thông báo
        String subject = "Order Confirmation";
        String content = "Your order has been successfully placed.";

        // Gửi email thông báo
        emailService.sendEmail(userEmail, subject, content);
    }
}
