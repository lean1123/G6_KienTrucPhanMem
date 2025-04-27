package ktpm17ctt.g6.notification.services;

import ktpm17ctt.g6.event.dto.NotificationEvent;
import ktpm17ctt.g6.notification.entities.Notification;

import java.util.List;

public interface NotificationService {
    void saveNotification(NotificationEvent notificationEvent);
    List<Notification> findByUserId(String userId);
    Notification findById(String id);
}