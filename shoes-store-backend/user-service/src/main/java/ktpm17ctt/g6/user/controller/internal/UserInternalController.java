package ktpm17ctt.g6.user.controller.internal;

import ktpm17ctt.g6.user.dto.ApiResponse;
import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.response.AddressResponse;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.entity.Address;
import ktpm17ctt.g6.user.service.AddressService;
import ktpm17ctt.g6.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserInternalController {
    UserService userService;
    AddressService addressService;


    @PostMapping("/users")
    ApiResponse<UserResponse> createProfile(@RequestBody UserRequest request) {
        log.info("Creating user profile: ", request);
        return ApiResponse.<UserResponse>builder()
                .result(userService.save(request).orElse(null))
                .build();
    }

    @GetMapping("/users/get-user-by-email")
    ApiResponse<UserResponse> getUserByEmail(@RequestParam String email) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.findByEmail(email).orElse(null))
                .build();
    }

    @GetMapping("/users/get-user-by-account-id")
    ApiResponse<UserResponse> getUserByAccountId(@RequestParam String accountId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.findByAccountId(accountId).orElse(null))
                .build();
    }

    @GetMapping("/users/{userId}")
    ApiResponse<UserResponse> getUserProfile(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.findById(userId).orElse(null))
                .build();
    }

    @GetMapping("/users/get-address-by-id/{addressId}")
    ApiResponse<AddressResponse> getAddressById(@PathVariable String addressId) throws Exception {
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.getAddressById(addressId))
                .build();
    }
}
