package ktpm17ctt.g6.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private String id;
    private String homeNumber;
    private String ward;
    private String district;
    private String city;
}
