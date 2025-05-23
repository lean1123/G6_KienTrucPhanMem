package ktpm17ctt.g6.orderservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PaymentMethodValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaymentMethod {
    String message() default "Invalid payment method and only accept CASH or VNPAY";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
