package ktpm17ctt.g6.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ktpm17ctt.g6.cart.enties.Cart;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findById(String cartId);
    Cart findByUserId(String userId);
}
