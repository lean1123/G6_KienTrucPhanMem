package ktpm17ctt.g6.paymentservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refund_invoices")
public class RefundInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String transactionId;
    private String transactionDate;
    private String transactionType;
    private String paymentId;
    private long amount;
    @Enumerated(EnumType.STRING)
    private RefundStatus status;
}
