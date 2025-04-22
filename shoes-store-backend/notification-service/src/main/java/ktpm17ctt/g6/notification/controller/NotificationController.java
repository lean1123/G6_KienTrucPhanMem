package ktpm17ctt.g6.notification.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.notification.dto.EmailRequest;
import ktpm17ctt.g6.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        log.info("Received request to send email to {}", emailRequest.getTo());
        emailService.sendEmail(emailRequest);
        log.info("Email sent successfully to {}", emailRequest.getTo());
        return ResponseEntity.ok("Email đã được gửi thành công!");
    }

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Notification Service is running...");
    }
}