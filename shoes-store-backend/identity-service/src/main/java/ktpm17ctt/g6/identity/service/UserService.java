package ktpm17ctt.g6.identity.service;

import ktpm17ctt.g6.identity.dto.request.RegistrationRequest;
import ktpm17ctt.g6.identity.dto.request.AccountUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(RegistrationRequest request);
    AccountResponse getMyInfo();
    AccountResponse updateAccount(String userId, AccountUpdateRequest request);
    void deleteAccount(String accountId);
    List<AccountResponse> getAccounts();
    AccountResponse getAccount(String accountId);
    void changePassword(String accountId, String newPassword);
}
