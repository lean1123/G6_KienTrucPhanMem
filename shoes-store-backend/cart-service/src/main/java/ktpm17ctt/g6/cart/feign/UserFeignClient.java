package ktpm17ctt.g6.cart.feign;

import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://localhost:8081/user/internal")
public interface UserFeignClient {
    @GetMapping("/users/get-user-by-email")
    ApiResponse<UserResponse> findByEmail(@RequestParam("email") String email);

    @GetMapping("/users/{userId}")
    ApiResponse<UserResponse> findById(@PathVariable("userId") String id);

    @GetMapping("/users/get-user-by-account-id")
    ApiResponse<UserResponse> getUserByAccountId(@RequestParam("accountId") String accountId);
}
