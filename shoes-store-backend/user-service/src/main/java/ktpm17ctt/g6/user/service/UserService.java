package ktpm17ctt.g6.user.service;

import ktpm17ctt.g6.user.dto.request.UserRequest;
import ktpm17ctt.g6.user.dto.response.UserResponse;
import ktpm17ctt.g6.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> findAll();

    Optional<UserResponse> findById(String id);

    Optional<UserResponse> findByEmail(String email);

    Optional<UserResponse> findByPhone(String phone);

    Optional<UserResponse> save(UserRequest userRequest);

    void deleteById(String id);

    Optional<UserResponse> updateInfo(String id, UserRequest userRequest);

    Optional<UserResponse> getUserById(String id);

    List<UserResponse> search(String keyword);
}
