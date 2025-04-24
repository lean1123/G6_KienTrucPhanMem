package ktpm17ctt.g6.notification.service;

import ktpm17ctt.g6.notification.dto.EmailRequest;
import ktpm17ctt.g6.notification.exception.EmailSendingException;
import ktpm17ctt.g6.notification.model.Notification;
import ktpm17ctt.g6.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    private final NotificationRepository notificationRepository;

    public void sendEmail(EmailRequest emailRequest) {
        sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getMessage(), emailRequest.getUserId());
    }

    @Retryable(value = EmailSendingException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void sendEmail(String email, String subject, String content, String userId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("""
                            {
                              "sender": { "email": "%s" },
                              "to": [ { "email": "%s" } ],
                              "subject": "%s",
                              "htmlContent": "%s"
                            }
                            """.formatted(senderEmail, email, subject, StringEscapeUtils.escapeJson(content))))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            boolean isSuccess = response.statusCode() == 201;
            Notification notification = Notification.builder()
                    .userId(userId)
                    .email(email)
                    .subject(subject)
                    .message(content)
                    .sent(isSuccess)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            if (!isSuccess) {
                log.error("Failed to send email to {}: {}", email, response.body());
                throw new EmailSendingException("Failed to send email: " + response.body());
            }

            log.info("Email sent successfully to {}", email);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", email, e.getMessage());
            Notification notification = Notification.builder()
                    .userId(userId)
                    .email(email)
                    .subject(subject)
                    .message(content)
                    .sent(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);
            throw new EmailSendingException("Error sending email", e);
        }
    }
}