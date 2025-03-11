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
    private CartDetailPK cartDetailPK;

    private int quantity;
    private int size;
    @ManyToOne
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    @MapsId("cartId")
	@JsonIgnore
    private Cart cart;

    @Column(insertable=false, updatable=false)
    private String productItemId;
}
