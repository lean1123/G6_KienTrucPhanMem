package ktpm17ctt.g6.user.service;

import ktpm17ctt.g6.user.entity.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);
    User createUser(User user);
    List<User> getAllUsers();
    User updateUser(User user);
    User getUserById(int id);
}
