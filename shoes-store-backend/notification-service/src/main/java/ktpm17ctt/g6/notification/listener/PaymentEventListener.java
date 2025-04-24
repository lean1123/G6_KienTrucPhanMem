package ktpm17ctt.g6.notification.listener;

import ktpm17ctt.g6.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "payment_success", groupId = "notification-group")
    public void sendPaymentSuccessConfirmation(String userEmail) {
        String subject = "Thông báo thanh toán thành công";
        String content = "Thanh toán cho đơn hàng của bạn đã được thực hiện thành công!";
      //  emailService.sendEmail(userEmail, subject, content);  // Gửi email thanh toán thành công
    }
}
