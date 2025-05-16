package ktpm17ctt.g6.notification.controllers;

import ktpm17ctt.g6.notification.dtos.requests.MailUserObject;
import ktpm17ctt.g6.notification.dtos.requests.SendEmailRequest;
import ktpm17ctt.g6.event.dto.NotificationEvent;
import ktpm17ctt.g6.notification.entities.Notification;
import ktpm17ctt.g6.notification.services.EmailService;
import ktpm17ctt.g6.notification.services.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;
    NotificationService notificationService;

    @KafkaListener(topics = "order_success_topic")
    public void listenNotificationDelivery(NotificationEvent notificationEvent){
        log.info("Message received: {}", notificationEvent);
        emailService.sendEmail(
                SendEmailRequest.builder()
                        .to(MailUserObject.builder()
                                .email(notificationEvent.getRecipient())
                                .build())
                        .subject(notificationEvent.getSubject())
                        .htmlContent(notificationEvent.getBody())
                        .build()
        );

        notificationService.saveNotification(notificationEvent);
    }

    @KafkaListener(topics = "signup_successful")
    public void listenNotificationSignUp(NotificationEvent notificationEvent){
        log.info("Message received: {}", notificationEvent);
        emailService.sendEmail(
                SendEmailRequest.builder()
                        .to(MailUserObject.builder()
                                .email(notificationEvent.getRecipient())
                                .build())
                        .subject(notificationEvent.getSubject())
                        .htmlContent(notificationEvent.getBody())
                        .build()
        );
        log.info("Email sent for signup_successful to: {}", notificationEvent.getRecipient());
        notificationService.saveNotification(notificationEvent);
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getNotificationsByUserId(@PathVariable String userId) {
        return notificationService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable String id) {
        return notificationService.findById(id);
    }
}
