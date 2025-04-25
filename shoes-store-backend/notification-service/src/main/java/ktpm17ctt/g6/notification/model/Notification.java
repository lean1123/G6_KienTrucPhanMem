package ktpm17ctt.g6.notification.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed(name = "userId_index")
    private String userId;
    private String email;
    private String subject;
    private String message;
    private boolean sent;
    @Indexed(name = "createdAt_index")
    private LocalDateTime createdAt;
}