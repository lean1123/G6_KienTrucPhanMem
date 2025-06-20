package ktpm17ctt.g6.orderservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private double total;
    private Instant createdDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String userId;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String addressId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetail> orderDetails;
    boolean isPayed;
}
