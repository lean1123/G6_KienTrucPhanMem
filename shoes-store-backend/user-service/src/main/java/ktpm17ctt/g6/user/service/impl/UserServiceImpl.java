package ktpm17ctt.g6.user.service.impl;

import ktpm17ctt.g6.user.dto.ApiResponse;
import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.request.UserUpdationRequest;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.dto.response.identity.AccountResponse;
import ktpm17ctt.g6.user.entity.User;
import ktpm17ctt.g6.user.mapper.UserMapper;
import ktpm17ctt.g6.user.repository.UserRepository;
import ktpm17ctt.g6.user.repository.clients.IdentityClient;
import ktpm17ctt.g6.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
     UserRepository userRepository;
     UserMapper userMapper;
     IdentityClient identityClient;

    @Override
    public List<UserResponse> findAll() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public Optional<UserResponse> findById(String id) {
        return userRepository.findById(id).map(userMapper::toUserResponse);
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return Optional.empty();
    }


    @Override
    public Optional<UserResponse> findByPhone(String phone) {
        User user = userRepository.findByPhone(phone).orElse(null);
        return Optional.of(userMapper.toUserResponse(user));
    }

    @Override
    public Optional<UserResponse> save(UserRequest userRequest) {
        log.info("Creating user profile: ", userRequest);
        User user = userMapper.toUser(userRequest);
        return Optional.of(userMapper.toUserResponse(userRepository.save(user)));
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UserResponse> updateInfo(String id, UserUpdationRequest userRequest) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Optional.empty();
        }
        String phone = user.getPhone();


        if((phone == null || phone.isEmpty())){
            if(userRepository.findByPhone(userRequest.getPhone()).isPresent()){
                throw new IllegalArgumentException("Phone number is already in use");
            }
            phone = userRequest.getPhone();
        }

        User updateUser = User.builder()
                .id(user.getId())
                .accountId(user.getAccountId())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(user.getEmail())
                .phone(phone)
                .dob(userRequest.getDob())
                .avatar(user.getAvatar())
                .gender(userRequest.getGender())
                .build();

        log.info("Updating user profile: {}", updateUser);
        return Optional.of(userMapper.toUserResponse(userRepository.save(updateUser)));
    }


    @Override
    @Transactional
    public Optional<UserResponse> getUserById(String id) {
        return userRepository.findById(id).map(userMapper::toUserResponse);
    }

    @Override
    public List<UserResponse> search(String keyword) {
        return userRepository.search(keyword).stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public Optional<UserResponse> findByAccountId(String accountId) {
        return userRepository.findByAccountId(accountId).map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getMyProfile() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        if(authentication != null && authentication.isAuthenticated()){
            email = authentication.getName();
            log.info("Email Logged: {}", email);
        }

        return this.getUserFromEmail(email);
    }

    @Override
    public UserResponse getUserFromEmail(String email) throws  Exception{
        log.info("Email in get user id from email: {}", email);
        ApiResponse<AccountResponse> accountResponse = identityClient.getAccountByEmail(email);

        if(accountResponse.getResult() == null){
            throw new NullPointerException("Account not found");
        }

        String accountId = accountResponse.getResult().getId();

        UserResponse userResponse = this.findByAccountId(accountId).orElse(null);
        if(userResponse == null){
            throw new NullPointerException("User not found");
        }
        return userResponse;
    }

}