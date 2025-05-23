package ktpm17ctt.g6.orderservice.dto.feinClient.user;

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
