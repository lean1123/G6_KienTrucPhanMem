package ktpm17ctt.g6.notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String userId;
    private String email;
    private String subject;
    private String message;
    private boolean sent;
    private LocalDateTime createdAt;
}
