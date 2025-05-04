package ktpm17ctt.g6.cart.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ktpm17ctt.g6.cart.dto.request.CartDetailRequest;
import ktpm17ctt.g6.cart.dto.request.DeleteCartDetailRequest;
import ktpm17ctt.g6.cart.dto.response.*;
import ktpm17ctt.g6.cart.dto.response.product.ProductItemResponse;
import ktpm17ctt.g6.cart.dto.response.product.QuantityOfSize;
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
        log.info("Adding to cart: productItemId={}, size={}, quantity={}",
                cartDetailRequest.getProductItemId(), cartDetailRequest.getSize(), cartDetailRequest.getQuantity());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authentication in addToCart: {}, Principal: {}", auth, auth != null ? auth.getPrincipal() : "null");

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
            log.debug("Saved cart to session: {} items", cartDetails.size());
        } catch (Exception e) {
            log.error("Failed to save cart to session: {}", e.getMessage(), e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", "Failed to save cart to session");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        String cartId = null;
        try {
            cartId = syncCartWithDatabase(cartDetails);
            log.info("Successfully synced cart with database");
        } catch (Exception e) {
            log.error("Failed to sync cart with database: {}", e.getMessage(), e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", "Failed to sync cart with database: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Tải lại cartDetails từ cơ sở dữ liệu
        if (cartId != null) {
            cartDetails = cartDetailService.findByCartId(cartId);
            try {
                String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
                httpSession.setAttribute("cart", cartDetailsJson);
                log.debug("Synced cart to session after database update: {} items", cartDetails.size());
            } catch (Exception e) {
                log.error("Failed to save cart to session after sync: {}", e.getMessage(), e);
            }
        }

        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();

        for (CartDetail cartDetail : cartDetails) {
            String productItemId = cartDetail.getProductItemId();
            ProductItemResponse productItemResponse = productFeignClient.getProductItemById(cartDetail.getProductItemId()).getResult();
                    cartDetailResponses.add(CartDetailResponse.builder()
                    .cartDetailPK(cartDetail.getCartDetailPK())
                    .productItem(productItemResponse)
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
        // Kiểm tra productItemId
        ApiResponse<ProductItemResponse> productItemResponseApiResponse =
                productFeignClient.getProductItemById(cartDetailRequest.getProductItemId());
        if (productItemResponseApiResponse == null || productItemResponseApiResponse.getResult() == null) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Invalid product item");
            return ResponseEntity.badRequest().body(response);
        }

        // Kiểm tra size có trong quantityOfSize không
        ProductItemResponse productItem = productItemResponseApiResponse.getResult();
        List<QuantityOfSize> quantityOfSize = productItem.getQuantityOfSize();
        if (quantityOfSize == null || quantityOfSize.isEmpty()) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "No sizes available for productItemId " + cartDetailRequest.getProductItemId());
            return ResponseEntity.badRequest().body(response);
        }

        Optional<QuantityOfSize> sizeQtyOpt = quantityOfSize.stream()
                .filter(sizeQty -> sizeQty.getSize() == size)
                .findFirst();
        if (sizeQtyOpt.isEmpty()) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Size " + size + " is not available for productItemId " + cartDetailRequest.getProductItemId());
            return ResponseEntity.badRequest().body(response);
        }

        // Kiểm tra số lượng tồn kho trực tiếp từ quantityOfSize
        int realQuantity = sizeQtyOpt.get().getQuantity();
        if (realQuantity == 0) {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Product with size " + size + " is out of stock for productItemId " + cartDetailRequest.getProductItemId());
            return ResponseEntity.badRequest().body(response);
        }

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

        String cartId = null;
        try {
            cartId = syncCartWithDatabase(cartDetails);
        } catch (Exception e) {
            log.error("Failed to sync cart with database: {}", e.getMessage());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", "Failed to sync cart with database: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Tải lại cartDetails từ cơ sở dữ liệu
        if (cartId != null) {
            cartDetails = cartDetailService.findByCartId(cartId);
            try {
                String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
                session.setAttribute("cart", cartDetailsJson);
                log.debug("Synced cart to session after database update: {} items", cartDetails.size());
            } catch (Exception e) {
                log.error("Failed to save cart to session after sync: {}", e.getMessage(), e);
            }
        }

        List<CartDetailResponse> cartDetailResponses = cartDetails.stream()
                .map(cartDetail -> CartDetailResponse.builder()
                        .cartDetailPK(cartDetail.getCartDetailPK())
                        .productItem(productFeignClient.getProductItemById(cartDetail.getProductItemId()).getResult())
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
                        .productItem(productFeignClient.getProductItemById((cartDetail.getProductItemId())).getResult())
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
        List<CartDetail> cartDetails = new ArrayList<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authentication: {}, Principal: {}", auth, auth != null ? auth.getPrincipal() : "null");
        if (auth != null) {
            String accountId = null;
            if (auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
                org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) auth.getPrincipal();
                accountId = jwt.getClaimAsString("accountId");
            }

            if (accountId != null && !accountId.isEmpty()) {
                log.info("Viewing cart for authenticated user: {}", accountId);

                ApiResponse<UserResponse> userResponse;
                try {
                    log.debug("Calling userFeignClient.getUserByAccountId({})", accountId);
                    userResponse = userFeignClient.getUserByAccountId(accountId);
                    log.debug("userFeignClient.getUserByAccountId response: {}", userResponse);
                    if (userResponse != null && userResponse.getResult() != null) {
                        UserResponse user = userResponse.getResult();
                        Cart cart = cartService.findCartByUser(user.getId());
                        if (cart != null) {
                            cartDetails = cartDetailService.findByCartId(cart.getId());
                            log.debug("Loaded cart from database: {} items", cartDetails.size());

                            try {
                                String cartDetailsJson = objectMapper.writeValueAsString(cartDetails);
                                session.setAttribute("cart", cartDetailsJson);
                                log.debug("Synced cart from database to session");
                            } catch (Exception e) {
                                log.error("Failed to sync cart to session: {}", e.getMessage(), e);
                            }
                        }
                    } else {
                        log.warn("User not found for accountId: {}", accountId);
                    }
                } catch (Exception e) {
                    log.error("Failed to fetch user by accountId from user-service: {}", e.getMessage(), e);
                }
            } else {
                log.debug("No accountId found in token, skipping database cart load");
            }
        }

        if (cartDetails.isEmpty()) {
            String cartDetailsJson = (String) session.getAttribute("cart");
            if (cartDetailsJson != null && !cartDetailsJson.isEmpty()) {
                try {
                    cartDetails = objectMapper.readValue(cartDetailsJson,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, CartDetail.class));
                } catch (Exception e) {
                    log.error("Failed to deserialize cart from session: {}", e.getMessage(), e);
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
                    .productItem(productFeignClient.getProductItemById(productItemId).getResult())
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
        // Kiểm tra productItemId
        ApiResponse<ProductItemResponse> productItemResponseApiResponse = productFeignClient.getProductItemById(cartDetailRequest.getProductItemId());
        if (productItemResponseApiResponse == null || productItemResponseApiResponse.getResult() == null) {
            throw new IllegalArgumentException("ProductItem not found");
        }

        // Kiểm tra size có trong quantityOfSize không
        ProductItemResponse productItem = productItemResponseApiResponse.getResult();
        List<QuantityOfSize> quantityOfSize = productItem.getQuantityOfSize();
        if (quantityOfSize == null || quantityOfSize.isEmpty()) {
            throw new IllegalArgumentException("No sizes available for productItemId " + cartDetailRequest.getProductItemId());
        }

        Optional<QuantityOfSize> sizeQtyOpt = quantityOfSize.stream()
                .filter(sizeQty -> sizeQty.getSize() == size)
                .findFirst();
        if (sizeQtyOpt.isEmpty()) {
            throw new IllegalArgumentException("Size " + size + " is not available for productItemId " + cartDetailRequest.getProductItemId());
        }

        // Kiểm tra số lượng tồn kho trực tiếp từ quantityOfSize
        int realQuantity = sizeQtyOpt.get().getQuantity();
        if (realQuantity == 0) {
            throw new IllegalArgumentException("Product with size " + size + " is out of stock for productItemId " + cartDetailRequest.getProductItemId());
        }

        // Kiểm tra sản phẩm cùng productItemId và size đã tồn tại trong giỏ hàng chưa
        for (CartDetail detail : cartDetails) {
            if (detail.getProductItemId().equals(cartDetailRequest.getProductItemId()) &&
                    detail.getCartDetailPK().getSize() == size) {
                int newQuantity = detail.getQuantity() + cartDetailRequest.getQuantity();
                if (newQuantity > realQuantity) {
                    throw new IllegalArgumentException("Quantity must be less than or equal to available stock");
                }
                detail.setQuantity(newQuantity);
                log.debug("Updated quantity for productItemId={}, size={}: new quantity={}",
                        cartDetailRequest.getProductItemId(), size, newQuantity);
                return; // Cập nhật số lượng và thoát
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
        // Không gán cartId ở đây, để syncCartWithDatabase xử lý
        newCartDetail.setCartDetailPK(cartDetailPK);

        cartDetails.add(newCartDetail);
        log.debug("Added new CartDetail to session: productItemId={}, size={}, quantity={}",
                cartDetailRequest.getProductItemId(), size, cartDetailRequest.getQuantity());
    }

    private String syncCartWithDatabase(List<CartDetail> cartDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.debug("User not authenticated, skipping sync with database");
            return null;
        }

        String accountId = null;
        if (auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) auth.getPrincipal();
            accountId = jwt.getClaimAsString("accountId");
        }

        if (accountId == null || accountId.isEmpty()) {
            log.debug("No accountId found in token, skipping sync with database");
            return null;
        }
        log.info("Syncing cart with database for accountId: {}", accountId);

        ApiResponse<UserResponse> userResponse;
        try {
            log.debug("Calling userFeignClient.getUserByAccountId({})", accountId);
            userResponse = userFeignClient.getUserByAccountId(accountId);
            log.debug("userFeignClient.getUserByAccountId response: {}", userResponse);
        } catch (Exception e) {
            log.error("Failed to fetch user by accountId from user-service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user by accountId", e);
        }

        UserResponse user = userResponse != null ? userResponse.getResult() : null;
        if (user == null) {
            log.warn("User not found for accountId: {}", accountId);
            throw new RuntimeException("User not found for accountId: " + accountId);
        }
        log.debug("Found user: id={}, accountId={}", user.getId(), accountId);

        Cart cart = cartService.findCartByUser(user.getId());
        if (cart == null) {
            log.info("Cart not found for user {}, creating new cart", user.getId());
            cart = new Cart();
            ApiResponse<UserResponse> userByIdResponse;
            try {
                log.debug("Calling userFeignClient.findById({})", user.getId());
                userByIdResponse = userFeignClient.findById(user.getId());
                log.debug("userFeignClient.findById response: {}", userByIdResponse);
            } catch (Exception e) {
                log.error("Failed to fetch user by ID from user-service: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to fetch user by ID", e);
            }

            UserResponse userById = userByIdResponse != null ? userByIdResponse.getResult() : null;
            String userId = userById != null ? userById.getId() : null;
            if (userId != null && !userId.isEmpty()) {
                cart.setUserId(userId);
                log.debug("Saving new cart for userId: {}", userId);
                cartService.save(cart);
                log.info("Created new cart with ID {} for user {}", cart.getId(), userId);
            } else {
                log.error("Invalid user ID for user {}", user.getId());
                throw new RuntimeException("Invalid user ID for user: " + user.getId());
            }
        } else {
            log.debug("Found existing cart with ID {} for user {}", cart.getId(), user.getId());
        }

        List<CartDetail> existingCartDetails = cartDetailService.findByCartId(cart.getId());
        log.debug("Found {} existing cart details in database", existingCartDetails.size());

        // Xóa các mục không còn trong session
        for (CartDetail existingDetail : existingCartDetails) {
            boolean existsInSession = cartDetails.stream().anyMatch(sessionDetail ->
                    sessionDetail.getCartDetailPK() != null &&
                            sessionDetail.getProductItemId().equals(existingDetail.getProductItemId()) &&
                            sessionDetail.getCartDetailPK().getSize() == existingDetail.getCartDetailPK().getSize());
            if (!existsInSession) {
                log.info("Deleting cart detail from database: productItemId={}, size={}",
                        existingDetail.getProductItemId(), existingDetail.getCartDetailPK().getSize());
                cartDetailService.delete(existingDetail);
            }
        }

        // Thêm hoặc cập nhật các mục từ session
        for (CartDetail sessionDetail : cartDetails) {
            if (sessionDetail.getCartDetailPK() == null || sessionDetail.getProductItemId() == null) {
                log.warn("Invalid CartDetail in session: productItemId={}, skipping", sessionDetail.getProductItemId());
                continue;
            }

            CartDetailPK newCartDetailPK = new CartDetailPK(cart.getId(), sessionDetail.getProductItemId(), sessionDetail.getCartDetailPK().getSize());
            Optional<CartDetail> existingDetailOpt = cartDetailService.findById(newCartDetailPK);

            if (existingDetailOpt.isPresent()) {
                // Cập nhật số lượng nếu đã tồn tại
                CartDetail existingDetail = existingDetailOpt.get();
                if (existingDetail.getQuantity() != sessionDetail.getQuantity()) {
                    existingDetail.setQuantity(sessionDetail.getQuantity());
                    cartDetailService.save(existingDetail);
                    log.info("Updated cart detail in database: productItemId={}, size={}, new quantity={}",
                            existingDetail.getProductItemId(), existingDetail.getCartDetailPK().getSize(), existingDetail.getQuantity());
                }
            } else {
                // Thêm mới nếu chưa tồn tại
                sessionDetail.setCartDetailPK(newCartDetailPK);
                sessionDetail.setCart(cart);
                try {
                    cartDetailService.save(sessionDetail);
                    log.info("Added new cart detail to database: productItemId={}, size={}",
                            sessionDetail.getProductItemId(), sessionDetail.getCartDetailPK().getSize());
                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                    log.error("Failed to save CartDetail due to duplicate entry: productItemId={}, size={}, cartId={}",
                            sessionDetail.getProductItemId(), sessionDetail.getCartDetailPK().getSize(), cart.getId(), e);
                    throw e;
                }
            }
        }

        log.info("Successfully synced cart with database for accountId {}", accountId);
        return cart.getId();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(HttpSession session) {
        Map<String, Object> response = new LinkedHashMap<>();
        log.info("Clearing cart for user");

        session.removeAttribute("cart");
        log.debug("Cleared cart from session");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authentication: {}, Principal: {}", auth, auth != null ? auth.getPrincipal() : "null");
        if (auth != null) {
            String accountId = null;
            if (auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
                org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) auth.getPrincipal();
                accountId = jwt.getClaimAsString("accountId");
            }

            if (accountId != null && !accountId.isEmpty()) {
                log.info("Clearing cart in database for user: {}", accountId);

                ApiResponse<UserResponse> userResponse;
                try {
                    log.debug("Calling userFeignClient.getUserByAccountId({})", accountId);
                    userResponse = userFeignClient.getUserByAccountId(accountId);
                    log.debug("userFeignClient.getUserByAccountId response: {}", userResponse);
                    if (userResponse != null && userResponse.getResult() != null) {
                        UserResponse user = userResponse.getResult();
                        Cart cart = cartService.findCartByUser(user.getId());
                        if (cart != null) {
                            cartDetailService.deleteAllByCartId(cart.getId());
                            log.info("Cleared all cart details from database for cartId: {}", cart.getId());
                        }
                    } else {
                        log.warn("User not found for accountId: {}", accountId);
                    }
                } catch (Exception e) {
                    log.error("Failed to fetch user by accountId from user-service: {}", e.getMessage(), e);
                }
            } else {
                log.debug("No accountId found in token, skipping database cart clear");
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