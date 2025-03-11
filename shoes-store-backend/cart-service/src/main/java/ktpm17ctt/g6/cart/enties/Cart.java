package ktpm17ctt.g6.cart.enties;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "cart")
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private String id;

    private String userId; 

    // Constructor mới để khởi tạo Cart mà không cần User entity
    public Cart(String userId) {
        this.userId = userId;
    }
}
