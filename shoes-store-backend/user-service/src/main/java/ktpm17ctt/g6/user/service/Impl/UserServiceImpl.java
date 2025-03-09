package ktpm17ctt.g6.user.service;

import ktpm17ctt.g6.user.entity.User;
import ktpm17ctt.g6.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public User createUser(User user) {
        // Thêm logic kiểm tra trùng email, mã hoá password...
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }
}
