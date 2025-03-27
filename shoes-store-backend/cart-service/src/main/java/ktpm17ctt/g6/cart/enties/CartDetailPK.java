package ktpm17ctt.g6.cart.enties;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDetailPK implements Serializable {
    private String cartId;
    private String productItemId;
    private int size;
}
