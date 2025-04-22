package ktpm17ctt.g6.identity.repository;

import ktpm17ctt.g6.identity.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByGoogleAccountId(String googleAccountId);
}
