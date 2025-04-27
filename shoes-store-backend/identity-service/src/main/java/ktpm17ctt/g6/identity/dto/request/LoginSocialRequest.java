package ktpm17ctt.g6.notification.dtos.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

<<<<<<<< HEAD:shoes-store-backend/notification-service/src/main/java/ktpm17ctt/g6/notification/dtos/requests/MailUserObject.java
@Builder
========
import java.time.LocalDate;

@Data
>>>>>>>> c0cda14f459546624502f41c2e87d1f755653064:shoes-store-backend/identity-service/src/main/java/ktpm17ctt/g6/identity/dto/request/LoginSocialRequest.java
@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
<<<<<<<< HEAD:shoes-store-backend/notification-service/src/main/java/ktpm17ctt/g6/notification/dtos/requests/MailUserObject.java
public class MailUserObject {
    String name;
    String email;
========
public class LoginSocialRequest {
    String email;
    String name;
    String avatar;
    String googleAccountId;
>>>>>>>> c0cda14f459546624502f41c2e87d1f755653064:shoes-store-backend/identity-service/src/main/java/ktpm17ctt/g6/identity/dto/request/LoginSocialRequest.java
}
