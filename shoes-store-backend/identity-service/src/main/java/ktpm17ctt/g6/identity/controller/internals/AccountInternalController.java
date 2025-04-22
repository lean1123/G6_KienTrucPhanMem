package ktpm17ctt.g6.identity.controller.internals;

import ktpm17ctt.g6.identity.dto.ApiResponse;
import ktpm17ctt.g6.identity.dto.response.AccountResponse;
import ktpm17ctt.g6.identity.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountInternalController {
    AccountService accountService;

    @GetMapping("/get-account-by-email")
    public ApiResponse<AccountResponse> getAccountByEmail(@RequestParam String email) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getAccountByEmail(email))
                .build();
    }
}
