package ktpm17ctt.g6.notification.listener;

import ktpm17ctt.g6.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "user_registered", groupId = "notification-group")
    public void sendWelcomeEmail(String email) {
        String subject = "Chào mừng bạn đến với hệ thống!";
        String content = "Cảm ơn bạn đã đăng ký tài khoản!";
       // emailService.sendEmail(email, subject, content);
    }
}
