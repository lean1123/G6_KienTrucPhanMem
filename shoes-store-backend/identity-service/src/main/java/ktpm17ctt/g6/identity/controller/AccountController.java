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
import org.springframework.security.access.prepost.PreAuthorize;
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
        log.info("User registration request: {}", request.getEmail());
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.createAccount(request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<AccountResponse>> getAccounts() {
        log.info("Get all accounts");
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAccounts())
                .build();
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<AccountResponse> getAccount(@PathVariable("accountId") String accountId) {
        log.info("Get account with ID: {}", accountId);
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getAccount(accountId))
                .build();
    }

    @GetMapping("/my-account")
    ApiResponse<AccountResponse> getMyInfo() {
        log.info("Get my account information");
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<String> deleteAccount(@PathVariable String accountId) {
        log.info("Deleted account with ID: {}", accountId);
        accountService.deleteAccount(accountId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<AccountResponse> updateAccount(@PathVariable String accountId, @RequestBody @Valid AccountUpdateRequest request) {
        log.info("Update account with ID: {}", accountId);
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.updateAccount(accountId, request))
                .build();
    }

    @PatchMapping("/{accountId}/change-password")
    ApiResponse<String> changePassword(@PathVariable String accountId, @RequestBody String newPassword) {
        log.info("Change password for account with ID: {}", accountId);
        accountService.changePassword(accountId, newPassword);
        return ApiResponse.<String>builder().result("Password has been changed").build();
    }
}
