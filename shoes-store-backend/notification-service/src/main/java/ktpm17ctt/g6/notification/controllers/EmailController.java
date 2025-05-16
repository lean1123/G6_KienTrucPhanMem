package ktpm17ctt.g6.notification.controllers;

import ktpm17ctt.g6.notification.dtos.ApiResponse;
import ktpm17ctt.g6.notification.dtos.requests.SendEmailRequest;
import ktpm17ctt.g6.notification.dtos.responses.EmailResponse;
import ktpm17ctt.g6.notification.services.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailController {
    EmailService emailService;

    @PostMapping("/send-email")
    public ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest sendEmailRequest){
        return ApiResponse.<EmailResponse>builder()
                .result(emailService.sendEmail(sendEmailRequest))
                .build();
    }
}
