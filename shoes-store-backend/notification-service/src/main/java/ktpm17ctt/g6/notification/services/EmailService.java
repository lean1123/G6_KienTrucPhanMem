package ktpm17ctt.g6.notification.services;

import ktpm17ctt.g6.notification.dtos.requests.SendEmailRequest;
import ktpm17ctt.g6.notification.dtos.responses.EmailResponse;

public interface EmailService {
    EmailResponse sendEmail(SendEmailRequest sendEmailRequest);
}
