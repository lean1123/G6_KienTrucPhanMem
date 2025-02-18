package ktpm17ctt.g6.notification.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import ktpm17ctt.g6.notification.entity.enums.NotificationStatus;
import ktpm17ctt.g6.notification.entity.enums.NotificationType;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    private String id;

    private String userId; // ID của người nhận thông báo
    private String message; // Nội dung thông báo
    private NotificationType type; // Loại thông báo (EMAIL, SMS, PUSH)
    private NotificationStatus status; // Trạng thái gửi (PENDING, SENT, FAILED)
    private LocalDateTime createdAt; // Thời gian tạo thông báo
    private boolean read; // Trạng thái đã đọc

    // Thông tin chi tiết cho từng loại thông báo
    private String email; // Nếu là thông báo email
    private String phoneNumber; // Nếu là SMS
    private String deviceToken; // Nếu là push notification
}

