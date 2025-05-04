package iuh.fit.se.chatService.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchIntentDTO {
    private String intent;
    private String color;
    private long priceMax;
    private long priceMin;
    private List<String> size;
    private String type;
}
