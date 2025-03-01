package ktpm17ctt.g6.paymentservice.services.impl;

import ktpm17ctt.g6.paymentservice.dtos.requests.PaymentRequest;
import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.entities.Payment;
import ktpm17ctt.g6.paymentservice.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PaymentServiceImpl {
    PaymentRepository paymentRepository;

    public PaymentResponse save(PaymentRequest request) {
        return null;
    }
}
