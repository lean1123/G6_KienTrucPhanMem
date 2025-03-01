package ktpm17ctt.g6.identity.service.implement;

import ktpm17ctt.g6.identity.dto.request.UserCreationRequest;
import ktpm17ctt.g6.identity.dto.request.UserUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.UserResponse;
import ktpm17ctt.g6.identity.entity.Account;
import ktpm17ctt.g6.identity.entity.Role;
import ktpm17ctt.g6.identity.exception.AppException;
import ktpm17ctt.g6.identity.exception.ErrorCode;
import ktpm17ctt.g6.identity.mapper.ProfileMapper;
import ktpm17ctt.g6.identity.mapper.AccountMapper;
import ktpm17ctt.g6.identity.repository.RoleRepository;
import ktpm17ctt.g6.identity.repository.AccountRepository;
import ktpm17ctt.g6.identity.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    AccountRepository userRepository;
    RoleRepository roleRepository;
    AccountMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileMapper profileMapper;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Account user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("USER_ROLE").ifPresent(roles::add);

        user.setRoles(roles);

        user = userRepository.save(user);

        var profileCreationRequest = profileMapper.toProfileCreationRequest(request);
        profileCreationRequest.setUserId(user.getId());
//        connect to profile service
//        profileClient.create(profileCreationRequest);

        var userCreationResponse = userMapper.toUserResponse(user);
        userCreationResponse.setId(user.getId());
        return userCreationResponse;
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        Account user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);userRepository.deleteById(userId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
