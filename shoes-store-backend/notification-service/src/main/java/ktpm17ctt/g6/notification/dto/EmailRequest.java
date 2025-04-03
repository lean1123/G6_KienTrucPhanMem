package ktpm17ctt.g6.notification.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String userId;
    private String to;
    private String subject;
    private String message;
}
