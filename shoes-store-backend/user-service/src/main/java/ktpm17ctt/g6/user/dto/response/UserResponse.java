package ktpm17ctt.g6.user.dto.response;


import ktpm17ctt.g6.user.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatar;
    private Gender gender;
}