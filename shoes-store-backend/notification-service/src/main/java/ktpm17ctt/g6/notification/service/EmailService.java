package ktpm17ctt.g6.notification.service;

import ktpm17ctt.g6.notification.dto.EmailRequest;
import ktpm17ctt.g6.notification.model.Notification;
import ktpm17ctt.g6.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    private final NotificationRepository notificationRepository;

    public void sendEmail(EmailRequest emailRequest) {
        sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getMessage());
    }

    public void sendEmail(String email, String subject, String content) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)  // ✅ Đọc từ application.yaml
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("""
                    {
                      "sender": { "email": "%s" },
                      "to": [ { "email": "%s" } ],
                      "subject": "%s",
                      "htmlContent": "%s"
                    }
                    """.formatted(senderEmail, email, subject, content))) // ✅ Đọc từ application.yaml
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Lưu vào MongoDB
            Notification notification = Notification.builder()
                    .email(email)
                    .subject(subject)
                    .message(content)
                    .sent(response.statusCode() == 201) // Nếu status 201 nghĩa là gửi thành công
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            System.out.println("Email sent: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
