package ktpm17ctt.g6.user.controller.internal;

import ktpm17ctt.g6.user.dto.ApiResponse;
import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserInternalController {
    UserService userService;

    public UserInternalController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    ApiResponse<UserResponse> createProfile(@RequestBody UserRequest request) {
        log.info("Creating user profile: ", request);
        return ApiResponse.<UserResponse>builder()
                .result(userService.save(request).orElse(null))
                .build();
    }

    @GetMapping("/users/{userId}")
    ApiResponse<UserResponse> getUserProfile(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.findById(userId).orElse(null))
                .build();
    }

}
