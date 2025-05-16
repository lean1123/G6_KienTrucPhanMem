package ktpm17ctt.g6.notification.services.Impl;

import ktpm17ctt.g6.event.dto.NotificationEvent;
import ktpm17ctt.g6.notification.entities.Notification;
import ktpm17ctt.g6.notification.repositories.NotificationRepository;
import ktpm17ctt.g6.notification.services.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {
    NotificationRepository notificationRepository;

    @Override
    public void saveNotification(NotificationEvent notificationEvent) {
        // Kiểm tra userId bắt buộc
        if (notificationEvent.getUserId() == null || notificationEvent.getUserId().isBlank()) {
            log.error("UserId is required for notification: {}", notificationEvent);
            throw new IllegalArgumentException("UserId is required");
        }

        // Tạo entity Notification từ NotificationEvent
        Notification notification = Notification.builder()
                .channel(notificationEvent.getChannel())
                .recipient(notificationEvent.getRecipient())
  //              .templateCode(notificationEvent.getTemplateCode())
  //              .param(notificationEvent.getParam())
                .subject(notificationEvent.getSubject())
                .body(notificationEvent.getBody())
                .userId(notificationEvent.getUserId())
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu vào MongoDB
        try {
            notificationRepository.save(notification);
            log.info("Notification saved to MongoDB: {}", notification);
        } catch (Exception e) {
            log.error("Failed to save notification to MongoDB: {}", notification, e);
            throw new RuntimeException("Failed to save notification", e);
        }
    }

    @Override
    public List<Notification> findByUserId(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public Notification findById(String id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.orElse(null);
    }
}