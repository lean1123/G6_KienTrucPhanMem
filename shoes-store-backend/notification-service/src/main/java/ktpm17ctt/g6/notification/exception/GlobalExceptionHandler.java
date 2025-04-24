package ktpm17ctt.g6.notification.exception;

import ktpm17ctt.g6.notification.exception.EmailSendingException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<String> handleEmailSendingException(EmailSendingException e) {
        return ResponseEntity.status(500).body("Lỗi khi gửi email: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(400).body("Dữ liệu không hợp lệ: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
    }
}