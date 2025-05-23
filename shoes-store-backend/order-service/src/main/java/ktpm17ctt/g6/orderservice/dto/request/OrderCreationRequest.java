package ktpm17ctt.g6.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ktpm17ctt.g6.orderservice.entities.PaymentMethod;
import ktpm17ctt.g6.orderservice.validation.ValidPaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    @ValidPaymentMethod(message = "Invalid payment method")
    String paymentMethod;
    @Valid
    List<OrderDetailRequest> orderDetails;
    @NotNull(message = "Address id is required")
    String addressId;
}
