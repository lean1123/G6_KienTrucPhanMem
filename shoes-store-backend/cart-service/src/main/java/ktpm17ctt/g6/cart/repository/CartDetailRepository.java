package ktpm17ctt.g6.cart.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, CartDetailPK> {
    @Query("SELECT cd FROM CartDetail cd WHERE cd.cart.id = ?1")
    List<CartDetail> findAllByCartId(String cartId);

    // Tìm CartDetail theo CartDetailPK
    Optional<CartDetail> findByCartDetailPK(CartDetailPK cartDetailPK);

    // Xóa CartDetail theo CartDetailPK
    void deleteByCartDetailPK(CartDetailPK cartDetailPK);
  //  Optional<CartDetail> findByCartIdAndProductItemIdAndSize(String cartId, String productItemId, int size);

}
