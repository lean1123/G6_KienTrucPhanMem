package ktpm17ctt.g6.paymentservice.repositories;

import ktpm17ctt.g6.paymentservice.entities.RefundInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundInvoiceRepositories extends JpaRepository<RefundInvoice, String> {
}
