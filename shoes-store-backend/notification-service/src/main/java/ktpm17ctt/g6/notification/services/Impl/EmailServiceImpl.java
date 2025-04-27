
package ktpm17ctt.g6.notification.services.Impl;

import feign.FeignException;
import ktpm17ctt.g6.notification.dtos.requests.EmailRequest;
import ktpm17ctt.g6.notification.dtos.requests.MailUserObject;
import ktpm17ctt.g6.notification.dtos.requests.SendEmailRequest;
import ktpm17ctt.g6.notification.dtos.responses.EmailResponse;
import ktpm17ctt.g6.notification.exceptions.AppException;
import ktpm17ctt.g6.notification.exceptions.ErrorCode;
import ktpm17ctt.g6.notification.repositories.httpClients.BrevoEmailServiceClient;
import ktpm17ctt.g6.notification.services.EmailService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {
    BrevoEmailServiceClient brevoEmailServiceClient;
    String emailApiKey;
    String senderEmail;

    public EmailServiceImpl(
            BrevoEmailServiceClient brevoEmailServiceClient,
            @Value("${brevo.api-key}") String emailApiKey,
            @Value("${brevo.sender-email}") String senderEmail) {
        this.brevoEmailServiceClient = brevoEmailServiceClient;
        this.emailApiKey = emailApiKey;
        this.senderEmail = senderEmail;
    }

    @Override
    public EmailResponse sendEmail(SendEmailRequest sendEmailRequest) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(MailUserObject.builder()
                        .name("G6 SHOES SHOP")
                        .email(senderEmail)
                        .build())
                .to(List.of(sendEmailRequest.getTo()))
                .subject(sendEmailRequest.getSubject())
                .htmlContent(sendEmailRequest.getHtmlContent())
                .build();

        try {
            return brevoEmailServiceClient.sendEmail(emailApiKey, emailRequest);
        } catch (FeignException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}