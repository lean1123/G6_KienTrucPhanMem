package ktpm17ctt.g6.orderservice.repositories.httpClients;

import ktpm17ctt.g6.orderservice.dto.common.ApiResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.identity.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "identity-service", url = "${IDENTITY_SERVICE_URL:http://localhost:8080/identity/internal/accounts}")
public interface IdentityClient {
    @GetMapping("/get-account-by-email")
    public ApiResponse<AccountResponse> getAccountByEmail(@RequestParam String email);
}
