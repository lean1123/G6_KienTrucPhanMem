package ktpm17ctt.g6.notification.dto.request;

import ktpm17ctt.g6.notification.entity.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {
    private String userId;
    private String message;
    private NotificationType type;
    private String email; // Email nếu gửi qua email
    private String phoneNumber; // Số điện thoại nếu gửi SMS
    private String deviceToken; // Token thiết bị nếu gửi push notification
}
