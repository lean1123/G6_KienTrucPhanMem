package ktpm17ctt.g6.user.repository;

import ktpm17ctt.g6.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByPhone(String phone);
    Boolean existsByPhone(String phone);
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.phone LIKE %?1%")
    List<User> search(String keyword);

    Optional<User> findByAccountId(String accountId);

}
