package ktpm17ctt.g6.identity.service.implement;

import ktpm17ctt.g6.identity.dto.request.RegistrationRequest;
import ktpm17ctt.g6.identity.dto.request.AccountUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.AccountResponse;
import ktpm17ctt.g6.identity.entity.Account;
import ktpm17ctt.g6.identity.entity.Role;
import ktpm17ctt.g6.identity.exception.AppException;
import ktpm17ctt.g6.identity.exception.ErrorCode;
import ktpm17ctt.g6.identity.mapper.UserMapper;
import ktpm17ctt.g6.identity.mapper.AccountMapper;
import ktpm17ctt.g6.identity.repository.RoleRepository;
import ktpm17ctt.g6.identity.repository.AccountRepository;
import ktpm17ctt.g6.identity.repository.httpClient.UserClient;
import ktpm17ctt.g6.identity.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    UserClient userClient;


    @Override
    public AccountResponse createAccount(RegistrationRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("USER").ifPresent(roles::add);

        account.setRoles(roles);

        account = accountRepository.save(account);

        log.info("Registration request for account: {}", request);

        var userCreationRequest = userMapper.toUserCreationRequest(request);
        userCreationRequest.setAccountId(account.getId());

        log.info("User creation request: {}", userCreationRequest);
//        connect to profile service
        var userProfile = userClient.createProfile(userCreationRequest);
        log.info("Created user profile: {}", userProfile);
        var accountCreationResponse = accountMapper.toAccountResponse(account);
        accountCreationResponse.setId(userProfile.getResult().getId());
        return accountCreationResponse;
    }

    @Override
    public AccountResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return accountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse updateAccount(String accountId, AccountUpdateRequest request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        accountMapper.updateAccount(account, request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        account.setRoles(new HashSet<>(roles));

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public List<AccountResponse> getAccounts() {
        log.info("In method get Accounts");
        return accountRepository.findAll().stream().map(accountMapper::toAccountResponse).toList();
    }

    @Override
    public AccountResponse getAccount(String accountId) {
        return accountMapper.toAccountResponse(accountRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public void changePassword(String accountId, String newPassword) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override
    public AccountResponse getAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return accountMapper.toAccountResponse(account);
    }
}
