package ktpm17ctt.g6.paymentservice.services.impl;

import ktpm17ctt.g6.paymentservice.repositories.RefundInvoiceRepositories;
import ktpm17ctt.g6.paymentservice.services.RefundInvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class RefundInvoiceServiceImpl implements RefundInvoiceService {
    RefundInvoiceRepositories refundInvoiceRepositories;
}
