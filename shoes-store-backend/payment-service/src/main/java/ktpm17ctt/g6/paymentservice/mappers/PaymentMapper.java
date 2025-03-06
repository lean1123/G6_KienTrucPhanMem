package ktpm17ctt.g6.paymentservice.mappers;

import ktpm17ctt.g6.paymentservice.dtos.responses.PaymentResponse;
import ktpm17ctt.g6.paymentservice.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toPaymentResponse(Payment payment);
}
