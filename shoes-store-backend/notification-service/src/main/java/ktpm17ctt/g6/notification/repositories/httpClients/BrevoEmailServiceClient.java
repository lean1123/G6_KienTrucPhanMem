package ktpm17ctt.g6.notification.repositories.httpClients;


import ktpm17ctt.g6.notification.dtos.requests.EmailRequest;
import ktpm17ctt.g6.notification.dtos.responses.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "brevo-email-service", url = "https://api.brevo.com")
public interface BrevoEmailServiceClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody EmailRequest email);

}
