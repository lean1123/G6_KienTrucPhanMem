package ktpm17ctt.g6.notification.dto.response;

import lombok.*;

import java.time.LocalDateTime;

import ktpm17ctt.g6.notification.entity.enums.NotificationStatus;
import ktpm17ctt.g6.notification.entity.enums.NotificationType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private String id;
    private String userId;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private boolean read;
}
