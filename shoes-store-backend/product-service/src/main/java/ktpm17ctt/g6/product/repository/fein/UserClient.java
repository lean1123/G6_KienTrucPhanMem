package ktpm17ctt.g6.product.repository.fein;


import ktpm17ctt.g6.product.dto.ApiResponse;
import ktpm17ctt.g6.product.dto.fein.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL:http://localhost:8081/user/internal}")
public interface UserClient {
    @GetMapping("/users/get-user-by-email")
    ApiResponse<UserResponse> getUserByEmail(@RequestParam String email);

    @GetMapping("/users/get-user-by-account-id")
    ApiResponse<UserResponse> getUserByAccountId(@RequestParam String accountId);
}
