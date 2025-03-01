package ktpm17ctt.g6.cart.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, CartDetailPK> {
    // Tìm tất cả CartDetail theo cartId
    List<CartDetail> findByCart_Id(String cartId);

    // Tìm CartDetail theo CartDetailPK
    Optional<CartDetail> findById(CartDetailPK cartDetailPK);

    // Xóa CartDetail theo CartDetailPK
    void deleteById(CartDetailPK cartDetailPK);
}
