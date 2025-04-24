package ktpm17ctt.g6.cart.enties;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "cart_details")
public class CartDetail implements Serializable {
    @EmbeddedId
    private CartDetailPK cartDetailPK; // Khóa chính phức hợp

    @ManyToOne
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    @MapsId("cartId")
    @JsonIgnore
    private Cart cart;

    @Column(insertable = false, updatable = false)
    private String productItemId;

    private int quantity; // Số lượng sản phẩm của một kích thước cụ thể

    public CartDetail(CartDetailPK cartDetailPK, int quantity) {
        this.cartDetailPK = cartDetailPK;
        this.quantity = quantity;
    }
}
