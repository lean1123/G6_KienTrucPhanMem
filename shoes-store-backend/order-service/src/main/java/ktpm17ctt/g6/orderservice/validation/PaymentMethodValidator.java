package ktpm17ctt.g6.orderservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ktpm17ctt.g6.orderservice.entities.PaymentMethod;

public class PaymentMethodValidator implements ConstraintValidator<ValidPaymentMethod, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Payment method is required")
                    .addConstraintViolation();
            return false;
        }

        try {
            PaymentMethod.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid payment method and only accept CASH or VNPAY")
                    .addConstraintViolation();
            return false;
        }
    }
}
