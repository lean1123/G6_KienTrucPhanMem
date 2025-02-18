package ktpm17ctt.g6.notification.service;

import ktpm17ctt.g6.notification.dto.request.NotificationRequestDTO;
import ktpm17ctt.g6.notification.dto.response.NotificationResponseDTO;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO);
}