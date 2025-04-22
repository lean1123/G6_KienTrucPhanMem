package ktpm17ctt.g6.cart.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.request.DeleteCartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.ApiResponse;
import ktpm17ctt.g6.cart.dto.response.CartDetailResponse;
import ktpm17ctt.g6.cart.dto.response.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.UserResponse;
import ktpm17ctt.g6.cart.enties.Cart;
import ktpm17ctt.g6.cart.enties.CartDetail;
import ktpm17ctt.g6.cart.enties.CartDetailPK;
import ktpm17ctt.g6.cart.feign.ProductFeignClient;
import ktpm17ctt.g6.cart.feign.UserFeignClient;
import ktpm17ctt.g6.cart.service.CartDetailService;
import ktpm17ctt.g6.cart.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class CartController {
    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final ProductFeignClient productFeignClient;
    private final UserFeignClient userFeignClient;
    private final ObjectMapper objectMapper;

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(HttpSession httpSession,
                                       @Valid @RequestBody CartDetailRequest cartDetailRequest,
                                       BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();

        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", errors);
            return ResponseEntity.badRequest().body(response);
        }

        List<CartDetail> cartDetails = getCartFromSession(httpSession);

        try {
            handleCarDetail(cartDetails, cartDetailRequest, httpSession, cartDetailRequest.getSize());
        } catch (IllegalArgumentException e) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
            httpSession.setAttribute("cart", cartDetailsJson);
        } catch (Exception e) {
            log.error("Failed to save cart to session: {}", e.getMessage());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", "Failed to save cart to session");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Đồng bộ với cơ sở dữ liệu
        try {
            syncCartWithDatabase(cartDetails);
        } catch (Exception e) {
            log.error("Failed to sync cart with database: {}", e.getMessage());
        }

        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            String productItemId = cartDetail.getProductItemId();
            cartDetailResponses.add(CartDetailResponse.builder()
                    .cartDetailPK(cartDetail.getCartDetailPK())
                    .productItemId(productItemId)
                    .quantity(cartDetail.getQuantity())
                    .build());
        }

        response.put("status", HttpStatus.OK.value());
        response.put("data", cartDetailResponses);
        log.info("Successfully added to cart: productItemId={}", cartDetailRequest.getProductItemId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<?> updateQuantity(HttpSession session,
                                            @Valid @RequestBody CartDetailRequest cartDetailRequest,
                                            BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();

        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", errors);
            return ResponseEntity.badRequest().body(response);
        }

        List<CartDetail> cartDetails = getCartFromSession(session);

        if (cartDetails == null || cartDetails.isEmpty()) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Cart is empty");
            return ResponseEntity.badRequest().body(response);
        }

        int size = cartDetailRequest.getSize();
        ApiResponse<ProductItemResponse> productItemResponseApiResponse =
                productFeignClient.getProductItemById(cartDetailRequest.getProductItemId());
        if (productItemResponseApiResponse == null || productItemResponseApiResponse.getResult() == null) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Invalid product item");
            return ResponseEntity.badRequest().body(response);
        }

        ApiResponse<Integer> realQtyResponse =
                productFeignClient.getTotalQuantityByProductItemAndSize(cartDetailRequest.getProductItemId(), size);
        if (realQtyResponse == null || realQtyResponse.getResult() == null) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Failed to retrieve real quantity");
            return ResponseEntity.badRequest().body(response);
        }
        int realQuantity = realQtyResponse.getResult();

        boolean found = false;
        for (CartDetail cartDetail : cartDetails) {
            if (cartDetail.getCartDetailPK().getProductItemId().equals(cartDetailRequest.getProductItemId()) &&
                    cartDetail.getCartDetailPK().getSize() == size) {
                if (cartDetailRequest.getQuantity() > realQuantity) {
                    response.put("status", HttpStatus.BAD_REQUEST.value());
                    response.put("data", "Quantity must be less than or equal to available stock");
                    return ResponseEntity.badRequest().body(response);
                }
                cartDetail.setQuantity(cartDetailRequest.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Product with specified size not found in cart");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
            session.setAttribute("cart", cartDetailsJson);
        } catch (Exception e) {
            log.error("Failed to update session: {}", e.getMessage());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", "Failed to update session");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        try {
            syncCartWithDatabase(cartDetails);
        } catch (Exception e) {
            log.error("Failed to sync cart with database: {}", e.getMessage());
        }

        List<CartDetailResponse> cartDetailResponses = cartDetails.stream()
                .map(cartDetail -> CartDetailResponse.builder()
                        .cartDetailPK(cartDetail.getCartDetailPK())
                        .productItemId(cartDetail.getProductItemId())
                        .quantity(cartDetail.getQuantity())
                        .build())
                .toList();

        response.put("status", HttpStatus.OK.value());
        response.put("data", cartDetailResponses);
        log.info("Successfully updated quantity for productItemId: {}", cartDetailRequest.getProductItemId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCartDetail(@RequestBody @Valid DeleteCartDetailRequest deleteRequest,
                                              HttpSession session) {
        Map<String, Object> response = new LinkedHashMap<>();
        log.info("Deleting cart detail: productItemId={}, size={}",
                deleteRequest.getProductItemId(), deleteRequest.getSize());

        List<CartDetail> cartDetails = getCartFromSession(session);

        if (cartDetails.isEmpty()) {
            log.warn("Cart is empty for session");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Cart is empty");
            return ResponseEntity.badRequest().body(response);
        }

        boolean removed = cartDetails.removeIf(cartDetail ->
                cartDetail.getProductItemId().equals(deleteRequest.getProductItemId()) &&
                        cartDetail.getCartDetailPK().getSize() == deleteRequest.getSize());

        if (!removed) {
            log.warn("Product not found in cart: productItemId={}, size={}",
                    deleteRequest.getProductItemId(), deleteRequest.getSize());
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Product with specified size not found in cart");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
            session.setAttribute("cart", cartDetailsJson);
        } catch (Exception e) {
            log.error("Failed to update session: {}", e.getMessage());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", "Failed to update session");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Đồng bộ với cơ sở dữ liệu
        try {
            syncCartWithDatabase(cartDetails);
        } catch (Exception e) {
            log.error("Failed to sync cart with database: {}", e.getMessage());
        }

        List<CartDetailResponse> cartDetailResponses = cartDetails.stream()
                .map(cartDetail -> CartDetailResponse.builder()
                        .cartDetailPK(cartDetail.getCartDetailPK())
                        .productItemId(cartDetail.getProductItemId())
                        .quantity(cartDetail.getQuantity())
                        .build())
                .toList();

        response.put("status", HttpStatus.OK.value());
        response.put("data", cartDetailResponses);
        log.info("Successfully deleted cart detail: productItemId={}, size={}",
                deleteRequest.getProductItemId(), deleteRequest.getSize());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> viewCart(HttpSession session) {
        Map<String, Object> response = new LinkedHashMap<>();

        // Kiểm tra người dùng đã đăng nhập chưa
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<CartDetail> cartDetails = new ArrayList<>();

        if (auth != null && auth.getName() != null) {
            String email = auth.getName();
            log.info("Viewing cart for authenticated user: {}", email);

            // Tìm thông tin người dùng
            Optional<UserResponse> userOpt;
            try {
                userOpt = userFeignClient.findByEmail(email);
            } catch (Exception e) {
                log.error("Failed to fetch user by email from user-service: {}", e.getMessage());
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", "Failed to fetch user from user-service");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            if (userOpt.isEmpty()) {
                log.warn("User not found for email: {}", email);
            } else {
                UserResponse user = userOpt.get();
                Cart cart = cartService.findCartByUser(user.getId());
                if (cart != null) {
                    // Lấy giỏ hàng từ cơ sở dữ liệu
                    cartDetails = cartDetailService.findByCartId(cart.getId());
                    log.debug("Loaded cart from database: {} items", cartDetails.size());

                    // Đồng bộ dữ liệu từ cơ sở dữ liệu vào session
                    try {
                        String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
                        session.setAttribute("cart", cartDetailsJson);
                        log.debug("Synced cart from database to session");
                    } catch (Exception e) {
                        log.error("Failed to sync cart to session: {}", e.getMessage());
                    }
                }
            }
        }

        // Nếu không có dữ liệu từ cơ sở dữ liệu (người dùng chưa đăng nhập hoặc không có giỏ hàng), lấy từ session
        if (cartDetails.isEmpty()) {
            String cartDetailsJson = (String) session.getAttribute("cart");
            if (cartDetailsJson != null && !cartDetailsJson.isEmpty()) {
                try {
                    cartDetails = objectMapper.readValue(cartDetailsJson,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, CartDetail.class));
                } catch (Exception e) {
                    log.error("Failed to deserialize cart from session: {}", e.getMessage());
                    response.put("status", HttpStatus.BAD_REQUEST.value());
                    response.put("data", "Error in view cart");
                    return ResponseEntity.badRequest().body(response);
                }
            }
        }

        if (cartDetails == null || cartDetails.isEmpty()) {
            log.info("Cart is empty");
            response.put("status", HttpStatus.OK.value());
            response.put("data", "Cart is empty");
            return ResponseEntity.ok(response);
        }

        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            String productItemId = cartDetail.getProductItemId();
            cartDetailResponses.add(CartDetailResponse.builder()
                    .cartDetailPK(cartDetail.getCartDetailPK())
                    .productItemId(productItemId)
                    .quantity(cartDetail.getQuantity())
                    .build());
        }

        response.put("status", HttpStatus.OK.value());
        response.put("data", cartDetailResponses);
        log.info("Successfully viewed cart: {} items", cartDetailResponses.size());
        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("unchecked")
    private List<CartDetail> getCartFromSession(HttpSession httpSession) {
        List<CartDetail> cartDetails = new ArrayList<>();
        try {
            String cartDetailsJson = (String) httpSession.getAttribute("cart");
            if (cartDetailsJson != null && !cartDetailsJson.isEmpty()) {
                cartDetails = objectMapper.readValue(cartDetailsJson, new TypeReference<List<CartDetail>>() {});
            }
        } catch (Exception e) {
            log.error("Failed to deserialize cart from session: {}", e.getMessage());
        }
        return cartDetails;
    }

    private void handleCarDetail(List<CartDetail> cartDetails, CartDetailRequest cartDetailRequest, HttpSession httpSession, int size) throws IllegalArgumentException {
        ApiResponse<ProductItemResponse> productItemResponseApiResponse = productFeignClient.getProductItemById(cartDetailRequest.getProductItemId());

        if (productItemResponseApiResponse == null || productItemResponseApiResponse.getResult() == null) {
            throw new IllegalArgumentException("ProductItem not found");
        }

        ApiResponse<Integer> realQty = productFeignClient.getTotalQuantityByProductItemAndSize(cartDetailRequest.getProductItemId(), size);
        if (realQty == null || realQty.getResult() == null) {
            throw new IllegalArgumentException("Failed to retrieve real quantity");
        }

        int realQuantity = realQty.getResult();

        // Kiểm tra sản phẩm cùng productItemId và size đã tồn tại trong giỏ hàng chưa
        for (CartDetail detail : cartDetails) {
            if (detail.getProductItemId().equals(cartDetailRequest.getProductItemId()) &&
                    detail.getCartDetailPK().getSize() == size) {

                int newQuantity = detail.getQuantity() + cartDetailRequest.getQuantity();
                if (newQuantity > realQuantity) {
                    throw new IllegalArgumentException("Quantity must be less than or equal to available stock");
                }
                detail.setQuantity(newQuantity);
                return; // Không cần tạo mới, chỉ cập nhật số lượng
            }
        }

        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới vào
        if (cartDetailRequest.getQuantity() > realQuantity) {
            throw new IllegalArgumentException("Quantity must be less than or equal to available stock");
        }

        CartDetail newCartDetail = new CartDetail();
        newCartDetail.setQuantity(cartDetailRequest.getQuantity());
        newCartDetail.setProductItemId(cartDetailRequest.getProductItemId());

        CartDetailPK cartDetailPK = new CartDetailPK();
        cartDetailPK.setProductItemId(cartDetailRequest.getProductItemId());
        cartDetailPK.setSize(size);
        newCartDetail.setCartDetailPK(cartDetailPK);

        cartDetails.add(newCartDetail);
    }

    private void syncCartWithDatabase(List<CartDetail> cartDetails) throws IllegalArgumentException {
        // Kiểm tra người dùng đã đăng nhập chưa
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            log.debug("User not authenticated, skipping sync with database");
            return;
        }
        String email = auth.getName();
        log.info("Syncing cart with database for user: {}", email);

        // Tìm thông tin người dùng
        Optional<UserResponse> userOpt;
        try {
            userOpt = userFeignClient.findByEmail(email);
        } catch (Exception e) {
            log.error("Failed to fetch user by email from user-service: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to fetch user from user-service");
        }

        if (userOpt.isEmpty()) {
            log.warn("User not found for email: {}", email);
            return;
        }

        UserResponse user = userOpt.get();
        log.debug("Found user: id={}", user.getId());

        // Tìm hoặc tạo giỏ hàng trong cơ sở dữ liệu
        Cart cart = cartService.findCartByUser(user.getId());
        if (cart == null) {
            log.info("Cart not found for user {}, creating new cart", user.getId());
            cart = new Cart();

            UserResponse userResponse;
            try {
                userResponse = userFeignClient.findById(user.getId());
            } catch (Exception e) {
                log.error("Failed to fetch user by ID from user-service: {}", e.getMessage());
                throw new IllegalArgumentException("Failed to fetch user from user-service");
            }

            String userId = userResponse != null ? userResponse.getId() : null;
            if (userId != null && !userId.isEmpty()) {
                cart.setUserId(userId);
                cartService.save(cart);
                log.info("Created new cart with ID {} for user {}", cart.getId(), userId);
            } else {
                log.error("Invalid user ID for user {}", user.getId());
                throw new IllegalArgumentException("User not found");
            }
        } else {
            log.debug("Found existing cart with ID {} for user {}", cart.getId(), user.getId());
        }

        // Lấy danh sách CartDetail hiện có trong cơ sở dữ liệu
        List<CartDetail> existingCartDetails = cartDetailService.findByCartId(cart.getId());
        log.debug("Found {} existing cart details in database", existingCartDetails.size());

        // Đồng bộ: Xóa các mục không còn trong session
        for (CartDetail existingDetail : existingCartDetails) {
            boolean existsInSession = cartDetails.stream().anyMatch(sessionDetail ->
                    sessionDetail.getProductItemId().equals(existingDetail.getProductItemId()) &&
                            sessionDetail.getCartDetailPK().getSize() == existingDetail.getCartDetailPK().getSize());
            if (!existsInSession) {
                log.info("Deleting cart detail from database: productItemId={}, size={}",
                        existingDetail.getProductItemId(), existingDetail.getCartDetailPK().getSize());
                cartDetailService.delete(existingDetail);
            }
        }

        // Đồng bộ: Thêm hoặc cập nhật các mục từ session vào cơ sở dữ liệu
        for (CartDetail sessionDetail : cartDetails) {
            Optional<CartDetail> existingDetailOpt = existingCartDetails.stream()
                    .filter(dbDetail ->
                            dbDetail.getProductItemId().equals(sessionDetail.getProductItemId()) &&
                                    dbDetail.getCartDetailPK().getSize() == sessionDetail.getCartDetailPK().getSize())
                    .findFirst();

            if (existingDetailOpt.isPresent()) {
                // Cập nhật nếu đã tồn tại
                CartDetail existingDetail = existingDetailOpt.get();
                if (existingDetail.getQuantity() != sessionDetail.getQuantity()) {
                    existingDetail.setQuantity(sessionDetail.getQuantity());
                    cartDetailService.save(existingDetail);
                    log.info("Updated cart detail in database: productItemId={}, size={}, new quantity={}",
                            existingDetail.getProductItemId(), existingDetail.getCartDetailPK().getSize(), existingDetail.getQuantity());
                }
            } else {
                // Thêm mới nếu chưa tồn tại
                CartDetailPK cartDetailPK = new CartDetailPK(cart.getId(), sessionDetail.getProductItemId(), sessionDetail.getCartDetailPK().getSize());
                sessionDetail.setCartDetailPK(cartDetailPK);
                sessionDetail.setCart(cart);
                cartDetailService.save(sessionDetail);
                log.info("Added new cart detail to database: productItemId={}, size={}",
                        sessionDetail.getProductItemId(), sessionDetail.getCartDetailPK().getSize());
            }
        }

        log.info("Successfully synced cart with database for user {}", email);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpSession session) {
        Map<String, Object> response = new LinkedHashMap<>();
        log.info("Clearing cart for user");

        // Xóa giỏ hàng trong session
        session.removeAttribute("cart");
        log.debug("Cleared cart from session");

        // Nếu người dùng đã đăng nhập, xóa giỏ hàng trong cơ sở dữ liệu
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            String email = auth.getName();
            log.info("Clearing cart in database for user: {}", email);

            // Tìm thông tin người dùng
            Optional<UserResponse> userOpt;
            try {
                userOpt = userFeignClient.findByEmail(email);
            } catch (Exception e) {
                log.error("Failed to fetch user by email from user-service: {}", e.getMessage());
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("data", "Failed to fetch user from user-service");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            if (userOpt.isEmpty()) {
                log.warn("User not found for email: {}", email);
                response.put("status", HttpStatus.OK.value());
                response.put("data", "Cart cleared (user not found)");
                return ResponseEntity.ok(response);
            }

            UserResponse user = userOpt.get();
            Cart cart = cartService.findCartByUser(user.getId());
            if (cart != null) {
                // Xóa tất cả CartDetail trong cơ sở dữ liệu
                cartDetailService.deleteAllByCartId(cart.getId());
                log.info("Cleared all cart details from database for cartId: {}", cart.getId());

                // Tùy chọn: Xóa luôn Cart nếu không cần giữ lại
                // cartService.delete(cart);
            }
        }

        response.put("status", HttpStatus.OK.value());
        response.put("data", "Cart cleared successfully");
        log.info("Successfully cleared cart");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hehe")
    public String hienThi() {
        log.info("hehe");
        return "hehe";
    }
}