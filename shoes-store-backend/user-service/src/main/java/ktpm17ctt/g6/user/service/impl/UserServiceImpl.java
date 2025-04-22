package ktpm17ctt.g6.user.service.impl;

import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.entity.User;
import ktpm17ctt.g6.user.mapper.UserMapper;
import ktpm17ctt.g6.user.repository.UserRepository;
import ktpm17ctt.g6.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    public Optional<UserResponse> updateInfo(String id, UserRequest userRequest) {
        return Optional.empty();
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


}