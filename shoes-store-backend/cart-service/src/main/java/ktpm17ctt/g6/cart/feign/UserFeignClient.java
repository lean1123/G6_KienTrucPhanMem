package ktpm17ctt.g6.cart.feign;

import ktpm17ctt.g6.cart.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="user-service", url="http://localhost:8082/user")
public interface UserFeignClient {
    @GetMapping("/{email}")
    Optional<UserResponse> findByEmail(String email);

    @GetMapping("/{id}")
    UserResponse findById(@PathVariable String id);
}
