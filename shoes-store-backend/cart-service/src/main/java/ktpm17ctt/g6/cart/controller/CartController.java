package ktpm17ctt.g6.cart.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.security.core.context.SecurityContextHolder;


import jakarta.servlet.http.HttpSession;
import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.service.CartDetailService;
import ktpm17ctt.g6.cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailService cartDetailService;

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(HttpSession session, @RequestBody CartDetailRequest cartDetailRequest) {
        String sessionId = session.getId();
        Optional<CartDetailResponse> cartDetailResponse = cartDetailService.addToCart(cartDetailRequest);
        
        if (cartDetailResponse.isPresent()) {
            return ResponseEntity.ok(cartDetailResponse.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding to cart");
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<?> updateQuantity(HttpSession session, @RequestBody CartDetailRequest cartDetailRequest) {
        String cartId = session.getId(); // Lấy cartId từ session
        Optional<CartDetailResponse> updatedCartDetail = cartDetailService.updateQuantityByCartId(
                cartId, cartDetailRequest.getProductItemId(), cartDetailRequest.getQuantity());

        if (updatedCartDetail.isPresent()) {
            return ResponseEntity.ok(updatedCartDetail.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating quantity");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeCartDetail(@RequestParam String cartId, @RequestParam String productItemId) {
        boolean removed = cartDetailService.removeCartDetail(cartId, productItemId);
        if (removed) {
            return ResponseEntity.ok("Cart detail removed successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error removing item from cart");
    }

    @GetMapping("/view")
    public ResponseEntity<List<CartDetailResponse>> viewCart(@RequestParam(required = false) String userId, HttpSession session) {
        List<CartDetailResponse> cartDetails;
        if (userId != null) {
        	Optional<Cart> optionalCart = cartService.findCartByUser(userId);
        	if (optionalCart.isPresent()) {
        	    cartDetails = cartDetailService.findAllByCartId(optionalCart.get().getId());
        	} else {
        	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        	}
        } else {
            cartDetails = cartService.getCartFromSession(session.getId());
        }
        return ResponseEntity.ok(cartDetails);
    }
    

    
//    @PostMapping("/sync")
//    public ResponseEntity<?> syncCartWithDatabase(HttpSession session) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserResponse user = userService.getUserByEmail(email);
//        if (user == null) {
//            return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "User not found"));
//        }
//        List<CartDetailResponse> cartDetails = cartService.getCartFromSession(session.getId());
//        cartService.syncCartWithDatabase(user.getId(), cartDetails);
//        return ResponseEntity.ok(Map.of("status", 200, "message", "Cart synced successfully"));
//    }
}
