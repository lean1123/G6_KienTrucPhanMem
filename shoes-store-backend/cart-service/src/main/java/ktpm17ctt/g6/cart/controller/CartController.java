package ktpm17ctt.g6.cart.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.service.CartDetailService;
import ktpm17ctt.g6.cart.service.CartService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailService cartDetailService;

    /**
     * API Thêm sản phẩm vào giỏ hàng
     * - Nếu có userId ⇒ Lưu vào database
     * - Nếu không có userId ⇒ Lưu vào session
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(HttpSession session, @RequestBody CartDetailRequest cartDetailRequest,
                                       @RequestParam(required = false) String userId) {
        if (userId != null) {
            // Lưu vào database
            Optional<CartDetailResponse> cartDetailResponse = cartDetailService.addToCart(cartDetailRequest);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((String) "Error adding to cart");


        } else {
            // Lưu vào session
            cartService.saveCartToSession(session.getId(), cartDetailRequest);
            return ResponseEntity.ok("Added to session cart");
        }
    }

    /**
     * API Cập nhật số lượng sản phẩm trong giỏ hàng
     * - Nếu có userId ⇒ cập nhật database
     * - Nếu không có userId ⇒ cập nhật session
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(HttpSession session, @RequestBody CartDetailRequest cartDetailRequest,
                                            @RequestParam(required = false) String userId) {
        if (userId != null) {
            Optional<CartDetailResponse> updatedCartDetail = cartDetailService.updateQuantityByCartId(
                    userId, cartDetailRequest.getProductItemId(), cartDetailRequest.getQuantity());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((String) "Error updating quantity");

        } else {
            // Cập nhật số lượng trong session
            List<CartDetailResponse> sessionCart = cartService.getCartFromSession(session.getId());
            sessionCart.forEach(item -> {
                if (item.getProductItemId().equals(cartDetailRequest.getProductItemId())) {
                    item.setQuantity(cartDetailRequest.getQuantity());
                }
            });
            session.setAttribute(session.getId(), sessionCart);
            return ResponseEntity.ok("Updated session cart quantity");
        }
    }

    /**
     * API Xóa sản phẩm khỏi giỏ hàng
     * - Nếu có userId ⇒ Xóa trong database
     * - Nếu không có userId ⇒ Xóa trong session
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeCartDetail(HttpSession session, @RequestParam String productItemId,
                                              @RequestParam(required = false) String userId) {
        if (userId != null) {
            boolean removed = cartDetailService.removeCartDetail(userId, productItemId);
            return removed ? ResponseEntity.ok("Item removed from cart")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error removing item from cart");
        } else {
            List<CartDetailResponse> sessionCart = cartService.getCartFromSession(session.getId());
            sessionCart.removeIf(item -> item.getProductItemId().equals(productItemId));
            session.setAttribute(session.getId(), sessionCart);
            return ResponseEntity.ok("Item removed from session cart");
        }
    }

    /**
     * API Xem giỏ hàng
     * - Nếu có userId ⇒ Lấy từ database
     * - Nếu không có userId ⇒ Lấy từ session
     */
    @GetMapping("/view")
    public ResponseEntity<List<CartDetailResponse>> viewCart(HttpSession session,
                                                             @RequestParam(required = false) String userId) {
        List<CartDetailResponse> cartDetails;
        if (userId != null) {
            Optional<Cart> optionalCart = cartService.findCartByUser(userId);
            cartDetails = optionalCart.map(cart -> cartDetailService.findAllByCartId(cart.getId()))
                    .orElse(Collections.emptyList());
        } else {
            cartDetails = cartService.getCartFromSession(session.getId());
        }
        return ResponseEntity.ok(cartDetails);
    }

    /**
     * API Merge giỏ hàng từ session vào database khi user đăng nhập
     */
    @PostMapping("/merge")
    public ResponseEntity<?> mergeCart(HttpSession session, @RequestParam String userId) {
        cartService.mergeSessionCartToDatabase(session.getId(), userId);
        return ResponseEntity.ok("Session cart merged into database");
    }

    @GetMapping("/hehe")
    public String hienThi(){
        System.out.println("hehe");
        return "hehe";
    }
}
