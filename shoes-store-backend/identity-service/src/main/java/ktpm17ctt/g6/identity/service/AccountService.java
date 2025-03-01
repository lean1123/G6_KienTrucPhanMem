package ktpm17ctt.g6.identity.service;

import ktpm17ctt.g6.identity.dto.request.UserCreationRequest;
import ktpm17ctt.g6.identity.dto.request.UserUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.UserResponse;

import java.util.List;

public interface AccountService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse getMyInfo();
    UserResponse updateUser(String userId, UserUpdateRequest request);
    void deleteUser(String userId);
    List<UserResponse> getUsers();
    UserResponse getUser(String userId);
}
