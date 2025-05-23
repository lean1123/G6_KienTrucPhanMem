package ktpm17ctt.g6.orderservice.repositories.httpClients;

import ktpm17ctt.g6.orderservice.dto.common.ApiResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.user.AddressResponse;
import ktpm17ctt.g6.orderservice.dto.feinClient.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL:http://localhost:8081/user/internal}")
public interface UserClient {
    @GetMapping("/users/get-user-by-email")
    ApiResponse<UserResponse> getUserByEmail(@RequestParam String email);

    @GetMapping("/users/{userId}")
    ApiResponse<UserResponse> getUserProfile(@PathVariable String userId);

    @GetMapping("/users/get-user-by-account-id")
    ApiResponse<UserResponse> getUserByAccountId(@RequestParam String accountId);
    @GetMapping("/users/get-address-by-id/{addressId}")
    ApiResponse<AddressResponse> getAddressById(@PathVariable String addressId) throws Exception;
}
