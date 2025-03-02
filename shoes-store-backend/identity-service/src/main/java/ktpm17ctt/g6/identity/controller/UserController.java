package ktpm17ctt.g6.identity.controller;

import jakarta.validation.Valid;
import ktpm17ctt.g6.identity.dto.ApiResponse;
import ktpm17ctt.g6.identity.dto.request.RegistrationRequest;
import ktpm17ctt.g6.identity.dto.request.AccountUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.AccountResponse;
import ktpm17ctt.g6.identity.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountController {
    AccountService accountService;

    @PostMapping("/registration")
    ApiResponse<AccountResponse> createUser(@RequestBody @Valid RegistrationRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.createAccount(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AccountResponse>> getAccounts() {
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAccounts())
                .build();
    }

    @GetMapping("/{accountId}")
    ApiResponse<AccountResponse> getAccount(@PathVariable("accountId") String accountId) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getAccount(accountId))
                .build();
    }

    @GetMapping("/my-account")
    ApiResponse<AccountResponse> getMyInfo() {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{accountId}")
    ApiResponse<String> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{accountId}")
    ApiResponse<AccountResponse> updateAccount(@PathVariable String accountId, @RequestBody @Valid AccountUpdateRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.updateAccount(accountId, request))
                .build();
    }

    @PatchMapping("/{accountId}/change-password")
    ApiResponse<String> changePassword(@PathVariable String accountId, @RequestBody String newPassword) {
        accountService.changePassword(accountId, newPassword);
        return ApiResponse.<String>builder().result("Password has been changed").build();
    }
}
