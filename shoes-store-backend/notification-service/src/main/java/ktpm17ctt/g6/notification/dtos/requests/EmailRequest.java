package ktpm17ctt.g6.notification.dtos.requests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmailRequest {
    MailUserObject sender;
    List<MailUserObject> to;
    String subject;
    String htmlContent;
}
