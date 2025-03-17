package ktpm17ctt.g6.user.service.Impl;

import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.entity.User;
import ktpm17ctt.g6.user.repository.UserRepository;
import ktpm17ctt.g6.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserResponse mapToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    private User mapToUser(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }


    @Override
    public List<UserResponse> findAll() {
        var users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).toList();
    }

    @Override
    public Optional<UserResponse> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return Optional.empty();
    }


    @Override
    public Optional<UserResponse> findByPhone(String phone) {
        User user = userRepository.findByPhone(phone).orElse(null);
        return Optional.of(mapToUserResponse(user));
    }

    @Override
    public Optional<UserResponse> save(UserRequest userRequest) {
        User user = mapToUser(userRequest);
        return Optional.of(mapToUserResponse(userRepository.save(user)));
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public Optional<UserResponse> updateInfo(String id, UserRequest userRequest) {
        return Optional.empty();
    }


    @Override
    @Transactional
    public Optional<User> getUserById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }

    @Override
    public List<UserResponse> search(String keyword) {
        return userRepository.search(keyword).stream().map(this::mapToUserResponse).toList();
    }


}