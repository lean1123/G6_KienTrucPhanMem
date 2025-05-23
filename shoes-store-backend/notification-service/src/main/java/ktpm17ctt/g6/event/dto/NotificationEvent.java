package ktpm17ctt.g6.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
    String channel;
    String recipient;
    String templateCode;
    Map<String, Object> param;
    String subject;
    String body;
    String userId;
}
